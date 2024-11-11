package com.company;

import com.company.ui.CustomMenu;
import com.company.ui.SliderPanel;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private SliderListener sliderListener;
    private Publisher tcpPublisher;
    private Subscriber tcpSubscriber;
    private Thread tcpSubscriberThread;
    private Thread tcpPublisherThread;
    private Thread mqttPublisherThread;
    private Thread mqttSubscriberThread;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main(); // 初始化主界面
        });
    }

    public Main() {
        super("Affect Recognition Simulator");

        sliderListener = new SliderListener();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new CustomMenu("Control", this));
        setJMenuBar(menuBar);

        SliderPanel sliderPanel = new SliderPanel(sliderListener);
        add(sliderPanel, BorderLayout.CENTER);

        setSize(600, 400);
        setVisible(true);
    }

    public void startMQTTTransmission() {
        stopTCPTransmission(); // 停止 TCP 传输
        sliderListener.setUseMQTT(true);
        Blackboard.getInstance().setRunning(true);  // 启用输出提示

        MQTTPublisher mqttPublisher = MQTTPublisher.getInstance();
        MQTTSubscriber mqttSubscriber = new MQTTSubscriber(sliderListener);

        mqttPublisherThread = new Thread(mqttPublisher);
        mqttSubscriberThread = new Thread(mqttSubscriber);

        mqttPublisherThread.start();
        mqttSubscriberThread.start();
    }

    public void startTCPTransmission() {
        stopTCPTransmission(); // 停止现有传输

        tcpPublisher = new Publisher("localhost", 5000);
        tcpSubscriber = new Subscriber(5000);
        sliderListener.setPublisher(tcpPublisher);
        sliderListener.setUseMQTT(false);
        Blackboard.getInstance().setRunning(true);

        tcpPublisherThread = new Thread(tcpPublisher);
        tcpSubscriberThread = new Thread(tcpSubscriber);

        tcpPublisherThread.start();
        tcpSubscriberThread.start();
    }

    public void stopTCPTransmission() {
        Blackboard.getInstance().setRunning(false);

        if (tcpPublisherThread != null && tcpPublisherThread.isAlive()) {
            tcpPublisherThread.interrupt();
            tcpPublisherThread = null;
        }

        if (tcpSubscriberThread != null && tcpSubscriberThread.isAlive()) {
            tcpSubscriber.stop();
            tcpSubscriberThread.interrupt();
            tcpSubscriberThread = null;
        }

        if (tcpPublisher != null) {
            tcpPublisher.close();
            tcpPublisher = null;
        }

        if (mqttPublisherThread != null && mqttPublisherThread.isAlive()) {
            mqttPublisherThread.interrupt();
            mqttPublisherThread = null;
        }

        if (mqttSubscriberThread != null && mqttSubscriberThread.isAlive()) {
            mqttSubscriberThread.interrupt();
            mqttSubscriberThread = null;
        }
    }
}
