package com.openmodloader.core.event;

import com.openmodloader.api.event.EventContext;
import com.openmodloader.api.event.EventMap;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventListener;
import com.openmodloader.api.event.IEventTarget;

import java.util.Collection;

public class EventDispatcher {
    private final EventMap events;

    public EventDispatcher(EventMap events) {
        this.events = events;
    }

    @SuppressWarnings("unchecked")
    public <E extends IEvent> void dispatch(E event) {
        IEventTarget<E> target = (IEventTarget<E>) event.makeTarget();
        Collection<IEventListener<E>> listeners = this.events.getListeners(target);
        if (listeners.isEmpty()) {
            return;
        }

        EventContext context = new EventContext();
        for (IEventListener<E> listener : listeners) {
            listener.handle(event, context);
        }
    }
}
