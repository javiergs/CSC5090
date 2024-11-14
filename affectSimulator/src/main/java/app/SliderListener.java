package app;

import affectSimulator.MQTTPublisher;
import affectSimulator.Publisher;
import affectSimulator.MQTTCommunicatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SliderListener implements PropertyChangeListener, MQTTCommunicatorInterface {
    private static final Logger logger = LoggerFactory.getLogger(SliderListener.class);
    private boolean isExternalUpdate = false;
    private boolean useMQTT = true;
    private Publisher tcpPublisher;
    private MQTTPublisher mqttPublisher;  // 添加 MQTTPublisher 实例

    public SliderListener() {
        Blackboard.getInstance().addPropertyChangeListener(this);
        this.mqttPublisher = MQTTPublisher.getInstance(this); // 使用 getInstance() 获取单例实例
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
                    mqttPublisher.publish(message);  // 使用 mqttPublisher 实例发布消息
                    logger.info("Published via MQTT: {}", message);
                } else if (tcpPublisher != null) {
                    tcpPublisher.publish(message);
                    logger.info("Published via TCP: {}", message);
                }
            }
        }

        isExternalUpdate = false;
    }

    @Override
    public void setSliderValueExternally(String sliderName, int newValue) {
        isExternalUpdate = true;
        setSliderValue(sliderName, newValue);
    }

    @Override
    public boolean isRunning() {
        return Blackboard.getInstance().isRunning();
    }

    public void setUseMQTT(boolean useMQTT) {
        this.useMQTT = useMQTT;
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
                    mqttPublisher.publish(message);  // 使用 mqttPublisher 实例发布消息
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
