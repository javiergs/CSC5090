package headSim;

/**
 * The `DataDestination` interface defines methods for handling data and error messages
 * received from subscribers. Classes that implement this interface can be used as
 * destinations for data received from various sources.
 */
public interface DataDestination {
    /**
     *  Processes data received from a subscriber.
     *
     * @param data The data string received from the subscriber.
     */
    void addSubscriberData(String data);

    /**
     * Handles an error message received from a subscriber.
     *
     * @param error The error message.
     */
    void alertError(String error);
}