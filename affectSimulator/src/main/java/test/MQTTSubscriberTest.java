package test;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class MQTTSubscriberTest {
    private MQTTSubscriber subscriber;
    private MQTTCommunicatorInterface mockInterface;

    @BeforeEach
    void setUp() {
        mockInterface = mock(MQTTCommunicatorInterface.class);
        subscriber = new MQTTSubscriber(mockInterface);
    }

    @Test
    void testMessageArrived() throws Exception {
        MqttMessage message = new MqttMessage("sliderName updated to: 10".getBytes());
        subscriber.messageArrived("project/topic", message);
        // 验证消息是否正确接收并处理
    }
}
