package com.openmodloader.api.event;

public class TargetedListener<E extends IEvent> {
    private final IEventTarget<E> target;
    private final EventListener<E> listener;

    public TargetedListener(IEventTarget<E> target, EventListener<E> listener) {
        this.target = target;
        this.listener = listener;
    }

    public IEventTarget<E> getTarget() {
        return target;
    }

    public EventListener<E> getListener() {
        return listener;
    }
}
