package com.openmodloader.api.event;

@FunctionalInterface
public interface IEventListener<E extends IEvent> {
    void handle(E event, EventContext context);
}
