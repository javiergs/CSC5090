package javiergs;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class Client extends PropertyChangeSupport implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean running = true;
    private final WorkArea workArea;
    private boolean isRandomMode = true; // Toggle between random and click modes
    private boolean usePortForSending = false; // true uses socket.out, false triggers event listener
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public Client(WorkArea workArea) {
        super(workArea);
        this.workArea = workArea;

        try {
            LOGGER.info("Attempting to connect to server...");
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            LOGGER.info("Connected to server successfully!");
        } catch (IOException e) {
            LOGGER.warning("Connection to server failed!");
            e.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public void toggleMode() {
        isRandomMode = !isRandomMode;
        out.println(isRandomMode ? "RANDOM_MODE" : "CLICK_MODE");
        LOGGER.info("Toggled mode to " + (isRandomMode ? "Random" : "Click"));
    }

    public String getCurrentMode() {
        if (isRandomMode) return "RANDOM_MODE";
        else return "CLICK_MODE";
    }

    public void sendMouseClickData(Point newPoint) {
        if (usePortForSending) {
            out.println("CLICK " + newPoint.x + " " + newPoint.y);
        } else {
            firePropertyChange("click", null, newPoint);
        }
    }

    public void sendOriginData(Point origin) {
        if (usePortForSending) {
            String originMessage = "ORIGIN " + origin.x + " " + origin.y;
            out.println(originMessage);
        } else {
            firePropertyChange("origin", null, origin);
        }
    }

    public void setRunning(boolean val) {
        running = val;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String command = in.readLine();
                if (command != null) {
                    LOGGER.info("Received command: " + command);
                    processCommand(command);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processCommand(String command) {
        if (command.startsWith("ARM_PROPERTIES")) processArmProperties(command);
        else if (command.startsWith("MOVE_ARM")) processMoveArm(command);
        else LOGGER.warning("Unknown command: " + command);
    }

    private void processMoveArm(String command) {
        String[] tokens = command.split(" ");

        if (tokens.length != 3) LOGGER.warning("Invalid command token length: " + command);

        try {
            int armIndex = Integer.parseInt(tokens[1]) - 1;
            int angle = Integer.parseInt(tokens[2]);
            if (armIndex >= 0 && armIndex < workArea.getArmCount()) {
                SwingUtilities.invokeLater(() -> workArea.updateArmAngle(armIndex, angle));
            }
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid arm values received.");
        }
    }

    private void processArmProperties(String command) {
        String[] tokens = command.split(" ");

        if (tokens.length != 3) LOGGER.warning("Invalid command token length: " + command);

        try {
            int armCount = Integer.parseInt(tokens[1]);
            int armLength = Integer.parseInt(tokens[2]);
            SwingUtilities.invokeLater(() -> workArea.updateArmProperties(armCount, armLength));
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid arm properties values received.");
        }

    }

    public void stop() {
        running = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
