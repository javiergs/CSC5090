package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SliderListener implements PropertyChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(SliderListener.class);
    private boolean isExternalUpdate = false; // avoid loop
    private boolean useMQTT = true;

    private Publisher tcpPublisher;

    public SliderListener() {
        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    public void setPublisher(Publisher tcpPublisher) {
        this.tcpPublisher = tcpPublisher;
    }

    public void setSliderValue(String sliderName, int newValue) {
        Blackboard blackboard = Blackboard.getInstance();
        int oldValue = blackboard.getSliderValue();

        if (newValue != oldValue) {
            blackboard.firePropertyChange("sliderValue_" + sliderName, oldValue, newValue);

            if (blackboard.isRunning() && !isExternalUpdate) {
                String message = sliderName + " updated to: " + newValue;

                if (useMQTT) {
                    MQTTPublisher.getInstance().publish(message);
                    logger.info("Published via MQTT: {}", message);
                } else if (tcpPublisher != null) {
                    tcpPublisher.publish(message);
                    logger.info("Published via TCP: {}", message);
                }
            }
        }

        isExternalUpdate = false;
    }

    public void setUseMQTT(boolean useMQTT) {
        this.useMQTT = useMQTT;
    }

    public void setSliderValueExternally(String sliderName, int newValue) {
        isExternalUpdate = true; // 设置为外部更新
        setSliderValue(sliderName, newValue);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if (propertyName.startsWith("sliderValue") && !isExternalUpdate) {
            int newValue = (int) evt.getNewValue();
            String sliderName = evt.getPropertyName().substring("sliderValue_".length());

            if (Blackboard.getInstance().isRunning() && evt.getOldValue() != null
                    && !evt.getOldValue().equals(newValue)) {
                String message = sliderName + " Slider Value: " + newValue;

                if (useMQTT) {
                    MQTTPublisher.getInstance().publish(message);
                    logger.info("Published via MQTT: {}", message);
                } else if (tcpPublisher != null) {
                    tcpPublisher.publish(message);
                    logger.info("Published via TCP: {}", message);
                }
            }
        } else if ("isRunning".equals(propertyName)) {
            boolean running = (boolean) evt.getNewValue();
            logger.info("System running status changed to: {}", running);
        }
    }
}
