package app.library;

public interface DataDestination {
    public void addSubscriberData(String dataWithPrefix);

    public void alertError(String messageWithPrefix);
}
