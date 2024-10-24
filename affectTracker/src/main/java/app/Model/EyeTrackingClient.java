package app.Model;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The {@code EyeTrackingClient} class is responsible for connecting to the eye-tracking data server,
 * receiving eye-tracking data, and adding it to the {@link Blackboard} for further processing.
 * <p>
 * This class extends {@link ClientThread} and is designed to run as a separate thread.
 * It continuously receives eye-tracking data while the system is running and stores the data in a queue
 * for processing.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class EyeTrackingClient extends ClientThread {
	
	public static final String THREAD_NAME = "EyeTrackingClient";
	
	public EyeTrackingClient(String host, int port) {
		super(host, port);
		super.setLog(Logger.getLogger(EmotionDataClient.class.getName()));
		super.setThreadName(THREAD_NAME);
	}
	
	@Override
	public void doYourWork() throws InterruptedException, IOException {
		long startTime = System.currentTimeMillis();
		String str = super.getInputStream().readUTF();
		Blackboard.getInstance().addToEyeTrackingQueue(str);
		long endTime = System.currentTimeMillis();
		super.getLog().info("Received eye tracking data: " + str + " in " + (endTime - startTime) + "ms");
	}
	
}
