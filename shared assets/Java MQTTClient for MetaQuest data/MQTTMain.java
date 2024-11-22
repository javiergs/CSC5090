package javiergs;

import org.eclipse.paho.client.mqttv3.MqttClient;
import javax.swing.*;
import java.awt.*;

public class MQTTMain extends JPanel {
	// 3D boundaries (adjust based on your scene)
	private static final float MIN_X = -1.5f;
	private static final float MAX_X = 1.5f;
	private static final float MIN_Y = 0.0f;
	private static final float MAX_Y = 2.0f;
	
	// Positions for hands and object (default values)
	private float leftHandX = 0.0f, leftHandY = 1.0f;
	private float rightHandX = 0.5f, rightHandY = 1.5f;
	private float cubeX = 0.0f, cubeY = 0.5f;
	
	public MQTTMain() {
		new Thread(() -> startMQTTSubscriber("tcp://test.mosquitto.org:1883", "jgs/unity/test")).start();
	}
	
	private void startMQTTSubscriber(String broker, String topic) {
		try {
			MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
			client.connect();
			client.subscribe(topic, (t, message) -> {
				String payload = new String(message.getPayload());
				System.out.println("Received: " + payload);
				parseData(payload);
				repaint();
			});
			System.out.println("Subscribed to topic: " + topic);
		} catch (Exception e) {
			System.err.println("MQTT error: " + e.getMessage());
		}
	}
	
	private void parseData(String json) {
		try {
			org.json.JSONObject obj = new org.json.JSONObject(json);
			// Parse left hand
			org.json.JSONObject leftHand = obj.getJSONObject("leftHand");
			leftHandX = (float) leftHand.getDouble("x");
			leftHandY = (float) leftHand.getDouble("y");
			// Parse right hand
			org.json.JSONObject rightHand = obj.getJSONObject("rightHand");
			rightHandX = (float) rightHand.getDouble("x");
			rightHandY = (float) rightHand.getDouble("y");
			// Parse cube
			org.json.JSONObject cube = obj.getJSONObject("cube");
			cubeX = (float) cube.getDouble("x");
			cubeY = (float) cube.getDouble("y");
		} catch (Exception e) {
			System.err.println("Error parsing data: " + e.getMessage());
		}
	}
	
	private int[] transformToScreen(float x, float y) {
		// Use the actual dimensions of the JPanel
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		
		// Transform coordinates dynamically based on current panel size
		int px = (int) ((x - MIN_X) / (MAX_X - MIN_X) * panelWidth);
		int py = (int) ((y - MIN_Y) / (MAX_Y - MIN_Y) * panelHeight);
		
		// Flip y-axis for screen coordinates
		py = panelHeight - py;
		
		return new int[]{px, py};
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Get the actual dimensions of the JPanel
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		
		// Clear screen
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panelWidth, panelHeight);
		
		// Draw left hand
		int[] leftHandPos = transformToScreen(leftHandX, leftHandY);
		g.setColor(Color.RED);
		g.fillOval(leftHandPos[0] - 5, leftHandPos[1] - 5, 10, 10);
		
		// Draw right hand
		int[] rightHandPos = transformToScreen(rightHandX, rightHandY);
		g.setColor(Color.BLUE);
		g.fillOval(rightHandPos[0] - 5, rightHandPos[1] - 5, 10, 10);
		
		// Draw cube
		int[] cubePos = transformToScreen(cubeX, cubeY);
		g.setColor(Color.GREEN);
		g.fillRect(cubePos[0] - 5, cubePos[1] - 5, 10, 10);
		
		// Debug info
		g.setColor(Color.WHITE);
		g.drawString("Left Hand: (" + leftHandX + ", " + leftHandY + ")", 10, 20);
		g.drawString("Right Hand: (" + rightHandX + ", " + rightHandY + ")", 10, 40);
		g.drawString("Cube: (" + cubeX + ", " + cubeY + ")", 10, 60);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Hand and Object Visualizer");
			MQTTMain panel = new MQTTMain();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1024, 768); // Initial size
			frame.add(panel);
			frame.setVisible(true);
		});
	}
}