package javiergs;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

public class Server implements Runnable, PropertyChangeListener {
    private static Server instance;
    private final ServerSocket serverSocket;
    private static final int port = 12345;
    private int armCount = 5; // Number of arms (defined by server)
    private int armLength = 80; // Length of each arm (defined by server)
    private static int originX = 300;
    private static int originY = 300; // Default origin values
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());


    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static Server getInstance() throws IOException {
        if (instance == null) instance = new Server(port);
        return instance;
    }

    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();
        server.run();
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Server has started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Client has connected");
                new ClientHandler(clientSocket, armCount, armLength).start();
            }
        } catch (IOException e) {
            LOGGER.throwing("Server", "run", e);
//            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("click".equals(evt.getPropertyName())) {
            Point clickPoint = (Point) evt.getNewValue();
            LOGGER.info("Server received client click at: (" + clickPoint.x + ", " + clickPoint.y + ")");
            ClientHandler.processClick(clickPoint.x, clickPoint.y);
        } else if ("origin".equals(evt.getPropertyName())) {
            Point origin = (Point) evt.getNewValue();
            LOGGER.info("Server received origin update at: (" + origin.x + ", " + origin.y + ")");
            originX = origin.x;
            originY = origin.y;
        }
    }

    private static class ClientHandler extends Thread {
        private BufferedReader in;
        private static PrintWriter out;
        private final Random random = new Random();
        private boolean randomMode = true; // default
        private static int armCount;
        private static int armLength;

        public ClientHandler(Socket socket, int armCount, int armLength) {
            this.armCount = armCount;
            this.armLength = armLength;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                LOGGER.throwing("ClientHandler", "constructor", e);
            }
        }

        @Override
        public void run() {
            try {
                sendArmProperties(); // Send arm properties to client after connection

                while (true) {
                    parseCommandFromClientInput();
                    handleRandomMode();
                }
            } catch (IOException | InterruptedException e) {
                LOGGER.throwing("ClientHandler", "run", e);
            }
        }

        private void handleRandomMode() throws InterruptedException {
            if (randomMode) {
                sendRandomCommand();
                sleep(1000);  // Wait for 1 second before sending the next command, avoid spam
            }
        }

        private void parseCommandFromClientInput() throws IOException {
            String input;
            if (in.ready() && (input = in.readLine()) != null) {
                switch (input.split(" ")[0]) {
                    case "RANDOM_MODE" -> {
                        randomMode = true;
                        LOGGER.info("Client mode is set to RANDOM_MODE");
                    }
                    case "CLICK_MODE" -> {
                        randomMode = false;
                        LOGGER.info("Client mode is set to CLICK_MODE");
                    }
                    case "CLICK" -> processClickCommand(input);
                    case "ORIGIN" -> processOriginUpdate(input);
                    default -> LOGGER.warning("Unknown command: " + input);
                }
            }
        }

        private void sendRandomCommand() {
            int randomArmNumber = random.nextInt(armCount) + 1;  // Choose an arm (1 to armCount)
            int randomAngle = random.nextInt(360);
            String command = "MOVE_ARM " + randomArmNumber + " " + randomAngle;

            LOGGER.info("Sending random command: " + command);
            out.println(command);
        }

        private void sendArmProperties() {
            String propertiesMessage = "ARM_PROPERTIES " + armCount + " " + armLength;
            out.println(propertiesMessage);
            LOGGER.info("Sent arm properties to client: " + propertiesMessage);
        }

        private void processClickCommand(String command) {
            String[] tokens = command.split(" ");

            // validate command format, example: "CLICK x y"
            if (tokens.length != 3) {
                LOGGER.warning("Invalid click command format received: " + command);
                return;
            }

            try {
                int x = Integer.parseInt(tokens[1]);
                int y = Integer.parseInt(tokens[2]);
                processClick(x, y);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid click coordinates received: " + tokens[1] + ", " + tokens[2]);
            }
        }

        private static void processClick(int x, int y) {
            LOGGER.info("Processing click at: (" + x + ", " + y + ")");
            calculateAndSendClientAngles(x, y);
        }

        private void processOriginUpdate(String command) {
            String[] tokens = command.split(" ");

            if (tokens.length != 3) {
                LOGGER.warning("Invalid origin command format received: " + command);
                return;
            }

            try {
                originX = Integer.parseInt(tokens[1]);
                originY = Integer.parseInt(tokens[2]);
                LOGGER.info("Updated origin received from client: (" + originX + ", " + originY + ")");
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid origin update received.");
            }

        }

        private static void calculateAndSendClientAngles(int targetX, int targetY) {
            int centerX = originX;
            int centerY = originY;

            int[] armLengths = new int[armCount];
            for (int i = 0; i < armCount; i++) {
                armLengths[i] = armLength;
            }

            // Angles for each arm (initially set to the current angles, if available)
            double[] angles = new double[armCount];
            for (int i = 0; i < angles.length; i++) {
                angles[i] = 0; // Initialize to 0 degrees, or read from the current state if stored
            }

            double epsilon = 5.0; // Acceptable distance threshold to the target
            int maxIterations = 1000; // Limit the number of iterations to prevent infinite loops

            // CCD algorithm: iterate to adjust each joint to move the endpoint closer to the target
            calculateUsingCCD(targetX, targetY, centerX, centerY, armLengths, angles, epsilon, maxIterations);

            sendAnglesToClient(angles);
        }

        private static void sendAnglesToClient(double[] angles) {
            for (int i = 0; i < angles.length; i++) {
                int angle = (int) (angles[i] % 360);
                String command = "MOVE_ARM " + (i + 1) + " " + angle;
                out.println(command);
            }
        }

        private static void calculateUsingCCD(int targetX, int targetY, int centerX, int centerY, int[] armLengths, double[] angles, double epsilon, int maxIterations) {
            for (int iteration = 0; iteration < maxIterations; iteration++) {
                // Calculate the endpoint of the final arm
                Point endPoint = calculateEndPoint(centerX, centerY, angles, armLengths);

                double distanceToTarget = endPoint.distance(targetX, targetY);

                // If the endpoint is close enough to the target, stop
                if (distanceToTarget < epsilon) break;

                // Iterate over each joint, starting from the last one
                for (int i = angles.length - 1; i >= 0; i--) {
                    // Get the position of the current joint
                    Point jointPosition = calculateJointPosition(centerX, centerY, angles, armLengths, i);

                    // Calculate the vectors
                    double dxTarget = targetX - jointPosition.x;
                    double dyTarget = targetY - jointPosition.y;
                    double dxEnd = endPoint.x - jointPosition.x;
                    double dyEnd = endPoint.y - jointPosition.y;

                    // Calculate the angle between the vectors
                    double angleTarget = Math.atan2(dyTarget, dxTarget);
                    double angleEnd = Math.atan2(dyEnd, dxEnd);
                    double angleDifference = angleTarget - angleEnd;

                    // Update the angle for the current joint, ensuring it stays within [0, 360] degrees
                    angles[i] = (angles[i] + Math.toDegrees(angleDifference)) % 360;

                    // Recalculate the endpoint after moving this joint
                    endPoint = calculateEndPoint(centerX, centerY, angles, armLengths);

                    // Check if we have reached the target after adjusting this joint
                    if (endPoint.distance(targetX, targetY) < epsilon) {
                        break;
                    }
                }
            }
        }

        // Helper method to calculate the endpoint of the final arm based on angles
        private static Point calculateEndPoint(int originX, int originY, double[] angles, int[] armLengths) {
            double cumulativeAngle = 0;
            int x = originX;
            int y = originY;

            for (int i = 0; i < angles.length; i++) {
                cumulativeAngle += Math.toRadians(angles[i]);
                x += (int) (armLengths[i] * Math.cos(cumulativeAngle));
                y += (int) (armLengths[i] * Math.sin(cumulativeAngle));
            }

            return new Point(x, y);
        }

        // Helper method to calculate the position of a specific joint
        private static Point calculateJointPosition(int originX, int originY, double[] angles, int[] armLengths, int jointIndex) {
            double cumulativeAngle = 0;
            int x = originX;
            int y = originY;

            for (int i = 0; i <= jointIndex; i++) {
                cumulativeAngle += Math.toRadians(angles[i]);
                x += (int) (armLengths[i] * Math.cos(cumulativeAngle));
                y += (int) (armLengths[i] * Math.sin(cumulativeAngle));
            }

            return new Point(x, y);
        }

    }
}
