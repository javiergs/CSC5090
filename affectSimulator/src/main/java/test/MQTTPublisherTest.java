package test;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class MQTTPublisherTest {
    private MQTTPublisher publisher;
    private MQTTCommunicatorInterface mockInterface;

    @BeforeEach
    void setUp() {
        mockInterface = mock(MQTTCommunicatorInterface.class);
        when(mockInterface.isRunning()).thenReturn(true);
        publisher = MQTTPublisher.getInstance(mockInterface);
    }

    @Test
    void testPublish() throws MqttException {
        publisher.publish("Test message");
        // 验证消息是否成功发布
        // 可添加断言或打印以确认行为
    }
}
