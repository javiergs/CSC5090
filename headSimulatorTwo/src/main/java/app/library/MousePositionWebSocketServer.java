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

    public MousePositionWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New WebSocket connection opened from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully.");
    }
}