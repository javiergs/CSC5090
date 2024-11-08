package app;

public interface DataDestination {
    void addSubscriberData(String dataWithPrefix);
    void alertError(String messageWithPrefix);
}
