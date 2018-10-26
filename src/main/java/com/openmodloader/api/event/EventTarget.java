package com.openmodloader.api.event;

public class EventTarget<E extends IEvent> implements IEventTarget<E> {
    private final Class<E> eventClass;
    private final int hash;

    EventTarget(Class<E> eventClass) {
        this.eventClass = eventClass;
        this.hash = this.computeHash();
    }

    @SuppressWarnings("unchecked")
    public static <E extends IEvent> EventTarget<E> of(Class<?> eventClass) {
        return new EventTarget<>((Class<E>) eventClass);
    }

    private int computeHash() {
        return this.eventClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj.getClass() == this.getClass() || obj instanceof EventTarget) {
            EventTarget<?> target = (EventTarget<?>) obj;
            return target.eventClass.equals(this.eventClass);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
