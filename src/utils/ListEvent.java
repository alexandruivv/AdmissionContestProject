package utils;

import java.util.List;

public abstract class ListEvent<E> {
    private ListEventType eventType;

    public ListEvent(ListEventType eventType){
        this.eventType = eventType;
    }

    public ListEventType getEventType() {
        return eventType;
    }

    public void setEventType(ListEventType eventType) {
        this.eventType = eventType;
    }

    public abstract List<E> getAll();
    public abstract E getElement();
}
