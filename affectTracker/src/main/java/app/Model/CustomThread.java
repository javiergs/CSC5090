package app.Model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Established data for threads to be managed
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public abstract class CustomThread extends Thread {
	
	private Logger log;
	private String threadName;
	private boolean running = true;
	
	@Override
	public void run() {
		try {
			
			while (running) {
				doYourWork();
			}
			
		} catch (InterruptedException e) {
			log.log(Level.SEVERE, threadName + " thread was interrupted", e);
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			log.warning(e.toString());
		} finally {
			cleanUpThread();
		}
	}
	
	public abstract void doYourWork() throws InterruptedException, IOException;
	
	public abstract void cleanUpThread();
	
	public void stopThread() {
		running = false;
	}
	
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	public void setLog(Logger log) {
		this.log = log;
	}
	
	public Logger getLog() {
		return log;
	}
	
	public String getThreadName() {
		return threadName;
	}

}
