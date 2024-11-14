package eyesimulator;

/**
 * Interface defining methods for a data destination that receives
 * subscriber data and handles errors.
 *
 * @version 1.2
 * @authors
 * Monish Suresh
 * Christine Widden
 * Luca Ornstil
 */

public interface DataDestination {
    void addSubscriberData(String dataWithPrefix);
    void alertError(String messageWithPrefix);
}
