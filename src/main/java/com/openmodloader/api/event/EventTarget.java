package com.openmodloader.api.event;

public class EventTarget<E extends IEvent> implements IEventTarget<E> {
    private final Class<E> eventType;

    protected EventTarget(Class<E> eventType) {
        this.eventType = eventType;
    }

    @SuppressWarnings("unchecked")
    public static <E extends IEvent> EventTarget<E> of(Class<?> eventClass) {
        return new EventTarget<>((Class<E>) eventClass);
    }

    @Override
    public Class<E> getType() {
        return this.eventType;
    }

    @Override
    public boolean canReceive(IEventTarget<?> target) {
        return target.getType().equals(this.eventType);
    }
}
