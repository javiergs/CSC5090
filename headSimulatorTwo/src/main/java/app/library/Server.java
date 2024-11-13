package app.library;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * app.library.Server class to handle WebSocket connections and messages.
 * Authors as listed in your README.md file.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Server implements Runnable {
	
	private Thread serverThread;
	private volatile boolean running = false;
	private MousePositionWebSocketServer webSocketServer;
	private CountDownLatch latch = new CountDownLatch(1);
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public void run() {
		webSocketServer = new MousePositionWebSocketServer(new InetSocketAddress(8887));
		webSocketServer.start();
		latch.countDown();
		logger.info("WebSocket server started on port 8887");
		while (running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.warn("app.library.Server interrupted", e);
				running = false;
			}
		}
		if (webSocketServer != null) {
			try {
				webSocketServer.stop();
				logger.info("WebSocket server stopped.");
			} catch (Exception e) {
				logger.error("Error stopping WebSocket server", e);
			}
		}
	}

	public void start() {
		if (serverThread == null || !serverThread.isAlive()) {
			running = true;
			serverThread = new Thread(this);
			serverThread.start();
			logger.info("app.library.Server thread started.");
		}
	}

	public void stop() {
		if (serverThread != null && serverThread.isAlive()) {
			running = false;
			serverThread.interrupt();
			logger.info("app.library.Server thread stopping...");
		}
	}
 
	public boolean isRunning() {
		return running;
	}
 
}
