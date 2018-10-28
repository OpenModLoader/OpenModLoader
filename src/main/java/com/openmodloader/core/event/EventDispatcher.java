package com.openmodloader.core.event;

import com.openmodloader.api.event.EventContext;
import com.openmodloader.api.event.EventMap;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventListener;
import com.openmodloader.api.event.IEventTarget;

import java.util.stream.Stream;

public class EventDispatcher {
    private final EventMap events;

    public EventDispatcher(EventMap events) {
        this.events = events;
    }

    @SuppressWarnings("unchecked")
    public <E extends IEvent> void dispatch(E event) {
        IEventTarget<E> target = (IEventTarget<E>) event.makeTarget();
        Stream<IEventListener<E>> listeners = this.events.getListeners(target);

        EventContext context = new EventContext();
        listeners.forEach(listener -> listener.handle(event, context));
    }
}
