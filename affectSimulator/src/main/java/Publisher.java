package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Publisher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
    private final String host;
    private final int port;
    private DataOutputStream outputStream;
    private Socket socket;

    public Publisher(String host, int port) {
        this.host = host;
        this.port = port;
        connect();
    }

    private void connect() {
        try {
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            logger.info("Connected to Subscriber at {}:{}", host, port);
        } catch (IOException e) {
            logger.error("Error connecting to Subscriber", e);
        }
    }

    public void publish(String message) {
        try {
            if (outputStream != null) {
                outputStream.writeUTF(message);
                outputStream.flush();
                logger.debug("Published message: {}", message);
            }
        } catch (IOException e) {
            logger.error("Error publishing message", e);
        }
    }

    @Override
    public void run() {
        while (Blackboard.getInstance().isRunning()) {
            publish("Periodic message from Publisher"); // 示例消息
            try {
                Thread.sleep(5000); // 间隔发布
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void close() {
        try {
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.error("Error closing Publisher", e);
        }
    }
}
