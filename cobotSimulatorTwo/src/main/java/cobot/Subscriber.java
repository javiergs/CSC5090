package cobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The Subscriber class is responsible for establishing a connection with a server. It continuously
 * listens for incoming commands containing robot arm angles, parses these commands, and updates the
 * blackboard with the received angles.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Subscriber implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    private String ip;
    private int port;
    private boolean running = false;

    /**
     * Constructs a new Subscriber instance that connects to the specified IP address and port.
     *
     * @param ip   the IP address of the server
     * @param port the port number of the server
     */
    public Subscriber(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Starts the subscriber and listens for incoming commands from the server. Commands are read,
     * parsed, and the robot's angles are updated on the blackboard.
     */
    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            logger.info("Subscriber is running");
            running = true;
            while (running) {
                String command = in.readLine();
                if (command != null) {
                    parse(command);
                }
            }
            logger.info("Subscriber is stopping");
            socket.close();
        } catch (IOException e) {
            logger.error("I/O error in Subscriber: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error in Subscriber: {}", e.getMessage(), e);
        } finally {
            running = false;
        }
    }

    /**
     * Stops the subscriber from running. It will stop listening for incoming commands.
     */
    public void stop() {
        running = false;
    }

    /**
     * Parses a command received from the server, expecting it to be a comma-separated string of
     * six integer values representing the robot's angles. These values are then set on the blackboard.
     *
     * @param command the command string containing the robot's angles
     */
    private void parse(String command) {
        String[] tokens = command.split(",");
        try {
            int[] numbers = new int[6];
            for (int i = 0; i < 6; i++) {
                numbers[i] = Integer.parseInt(tokens[i]);
            }
            Blackboard.getInstance().setAngles(numbers);
        } catch (NumberFormatException e) {
            logger.error("Error parsing command", e);
        }
    }

    /**
     * Returns whether the subscriber is currently running.
     *
     * @return true if the subscriber is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
}