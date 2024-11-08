package app.Model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code EmotionDataClient} class is responsible for connecting to the emotion data server,
 * receiving emotion data, and adding it to the {@link Blackboard} for further processing.
 * <p>
 * This class extends {@link ClientThread} and is designed to run as a separate thread.
 * It continuously receives emotion data while the system is running and stores the data in a queue
 * for processing.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class EmotionDataClient implements Runnable {//extends ClientThread {
	
	public static final String THREAD_NAME = "EmotionDataClient";

	private Logger log = LoggerFactory.getLogger(EmotionDataClient.class.getName());
	private Socket connection;
	private DataInputStream inputStream;

	public EmotionDataClient(Socket socket, DataInputStream inputIS){
		this.connection = socket;
		this.inputStream = inputIS;
	}
	/*public EmotionDataClient(String host, int port) {
		super(host, port);
		super.setLog(LoggerFactory.getLogger(EmotionDataClient.class.getName()));
		super.setThreadName(THREAD_NAME);
	}*/

 /*
	@Override
	public void doYourWork() throws InterruptedException, IOException {
		long startTime = System.currentTimeMillis();
		String str = super.getInputStream().readUTF();
		Blackboard.getInstance().addToEmotionQueue(str);
		long endTime = System.currentTimeMillis();
		super.getLog().info("Received emotion data: " + str + " in " + (endTime - startTime) + "ms");
	}*/

	public void doYourWork() throws InterruptedException, IOException {
		long startTime = System.currentTimeMillis();
		String str = inputStream.readUTF();
		Blackboard.getInstance().addToEmotionQueue(str);
		long endTime = System.currentTimeMillis();
		log.info("Received emotion data: " + str + " in " + (endTime - startTime) + "ms");
	}

	@Override
	public void run() {
		while (true){
			try{
				doYourWork();
			} catch (IOException | InterruptedException ex) {
				Blackboard.getInstance().reportEmotionThreadError(ex.getMessage());
				// warn since the panel still can draw
				log.warn(THREAD_NAME + ": error with server connection - " + ex.getMessage());
            }
        }
	}
}