package affectSimulator;

public interface MQTTCommunicatorInterface {
    boolean isRunning();
    void setSliderValueExternally(String sliderName, int newValue);
}
