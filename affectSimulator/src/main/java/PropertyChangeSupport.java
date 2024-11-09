package com.company;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class PropertyChangeSupport {
    private final List<PropertyChangeListener> listeners = new ArrayList<>();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new java.beans.PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }
}
