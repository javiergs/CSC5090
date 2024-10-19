package javiergs;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {
    private WorkArea workArea;
    private JLabel modeLabel;
    private JLabel statusLabel;

    public Main() throws IOException {
        Server server = startServer();
        setupWorkArea(server);
        setupGUI();

        setVisible(true);
    }

    private Server startServer() throws IOException {
        Server server = Server.getInstance();
        new Thread(server).start();
        return server;
    }

    private void setupWorkArea(Server server) {
        workArea = new WorkArea();
        add(workArea);
        workArea.addPropertyChangeListener(server);
    }

    private void setupGUI() {
        setTitle("Cobot Client");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int squareSize = screenSize.height;
        setSize(squareSize, squareSize);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setStatusLabel();
        setModeLabel();
        setJMenuBar(setupJMenuBarWithItems());
    }

    private void setStatusLabel() {
        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateStatusLabel("Not Started");
        add(statusLabel, BorderLayout.NORTH);
    }

    private void setModeLabel() {
        modeLabel = new JLabel();
        modeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateModeLabel("RANDOM_MODE");
        add(modeLabel, BorderLayout.SOUTH);
    }

    private void updateStatusLabel(String status) {
        statusLabel.setText("Client Status: " + status);
        statusLabel.repaint();
    }

    private void updateModeLabel(String mode) {
        modeLabel.setText("Current Mode: " + mode);
        modeLabel.repaint();
    }

    private JMenuBar setupJMenuBarWithItems() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Options");

        // ITEM 1
        JMenuItem connectItem = new JMenuItem("Start WorkArea Client");
        connectItem.addActionListener(e -> {
            workArea.startClient();
            updateStatusLabel("STARTED");
            revalidate();  // refresh GUI
        });
        fileMenu.add(connectItem);

        // ITEM 2
        JMenuItem toggleModeItem = new JMenuItem("Toggle Mode");
        toggleModeItem.addActionListener(e -> {
            if (workArea != null) {
                workArea.toggleMode();
                updateModeLabel(workArea.getCurrentMode());
            }
        });
        fileMenu.add(toggleModeItem);

        // ITEM 3
        JMenuItem pauseItem = new JMenuItem("Pause Client");
        pauseItem.addActionListener(e -> {
            workArea.pauseClient();
            updateStatusLabel("PAUSING..");
        });
        fileMenu.add(pauseItem);

        // ITEM 4
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            System.exit(0);
        });
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        return menuBar;
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
