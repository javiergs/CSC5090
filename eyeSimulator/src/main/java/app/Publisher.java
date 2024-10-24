package app;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * app.Server class that listens for incoming connections and creates a new ServerHandler thread for each connection.
 *
 * @author Luca Ornstil,
 * @author Monish Suresh,
 * @author Christine Widden
 * @version 1.0
 */
public class Publisher implements Runnable {
	
	private Timer timer;
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private boolean isSendingData = false;
	
	public Publisher() {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			System.out.println("app.Server is waiting for client connection...");
			socket = serverSocket.accept();
			System.out.println("test.Client connected.");
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		System.out.println("app.Server is running...");
	}
	
	public void startTransmission() {
		if (!isSendingData) {
			System.out.println("Starting data transmission...");
			isSendingData = true;
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					sendClickData();
				}
			}, 0, 1000); // removed division by transmission speed
			
			//Todo: you can remove the Timer just by adding a loop infinite in the Run method.
			//Todo: That infinite loop can be stoped with a global variable.
			// thread sleep can be used to define time lapse between each iteration.
		}
	}
	
	public void stopTransmission() {
		if (isSendingData && timer != null) {
			System.out.println("Stopping data transmission...");
			timer.cancel();
			isSendingData = false;
		}
	}
	
	private void sendClickData() {
		List<Point> clicks = Blackboard.getInstance().getClickPositions();
		if (!clicks.isEmpty()) {
			try {
				objectOutputStream.writeObject(clicks);
				objectOutputStream.flush();
				Blackboard.getInstance().clearClicks();  // Clear clicks after sending
				System.out.println("Sent clicks to client.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopServer() {
		stopTransmission();
		try {
			if (socket != null) {
				objectOutputStream.close();
				socket.close();
			}
			System.out.println("app.Server stopped.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
