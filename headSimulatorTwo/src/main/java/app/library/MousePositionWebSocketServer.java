package app.library;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * The `MousePositionWebSocketServer` class extends the `WebSocketServer` class to
 * handle WebSocket connections for transmitting mouse position data. It provides
 * methods for managing connection events, such as opening, closing, and receiving
 * messages.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */

public class MousePositionWebSocketServer extends WebSocketServer {

    /**
     * Constructs a `MousePositionWebSocketServer`.
     *
     * @param address The address to bind the server to.
     */
    public MousePositionWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    /**
     * Called when a new WebSocket connection is opened.
     *
     * @param conn The WebSocket connection that was opened.
     * @param handshake The handshake data associated with the connection.
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New WebSocket connection opened from " + conn.getRemoteSocketAddress());
    }

    /**
     * Called when a WebSocket connection is closed.
     *
     * @param conn The WebSocket connection that was closed.
     * @param code The close code.
     * @param reason The reason for closing the connection.
     * @param remote Whether the connection was closed remotely.
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed: " + reason);
    }

    /**
     * Called when a message is received from a WebSocket connection.
     *
     * @param conn The WebSocket connection that sent the message.
     * @param message The received message.
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);
    }

    /**
     * Called when an error occurs on a WebSocket connection.
     *
     * @param conn The WebSocket connection where the error occurred.
     * @param ex The exception that was thrown.
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Called when the WebSocket server starts successfully.
     */
    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully.");
    }
}