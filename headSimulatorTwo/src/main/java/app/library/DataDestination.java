package app.library;

public interface DataDestination {
    void addSubscriberData(String data);
    void alertError(String error);
}
