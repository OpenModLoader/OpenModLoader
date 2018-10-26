package com.openmodloader.api.event;

@FunctionalInterface
public interface EventListener<E extends IEvent> {
    void handle(E event, EventContext context);
}
