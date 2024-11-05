package app;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<BlackboardObserver> observers;

    public EventManager() {
        this.observers = new CopyOnWriteArrayList<>();
    }

    public void addObserver(BlackboardObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BlackboardObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (BlackboardObserver observer : observers) {
            observer.update();
        }
    }
}
