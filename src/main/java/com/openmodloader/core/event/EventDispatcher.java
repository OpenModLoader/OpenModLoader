package com.openmodloader.core.event;

import com.google.common.collect.ImmutableMap;
import com.openmodloader.api.event.EventContext;
import com.openmodloader.api.event.EventListener;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventTarget;
import com.openmodloader.api.event.TargetedListener;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventDispatcher {
    private final ImmutableMap<IEventTarget<?>, Collection<EventListener<?>>> events;

    private EventDispatcher(ImmutableMap<IEventTarget<?>, Collection<EventListener<?>>> events) {
        this.events = events;
    }

    // TODO: remove
    @Deprecated
    public static EventDispatcher from(Collection<TargetedListener<?>> listeners) {
        Map<IEventTarget<?>, Collection<EventListener<?>>> events = new HashMap<>();
        for (TargetedListener<?> listener : listeners) {
            events.computeIfAbsent(listener.getTarget(), t -> new ArrayList<>())
                    .add(listener.getListener());
        }
        return new EventDispatcher(ImmutableMap.copyOf(events));
    }

    public <E extends IEvent> void dispatch(E event) {
        EventContext context = new EventContext();
        Collection<EventListener<E>> listeners = this.collectListeners(event);
        if (listeners == null) {
            return;
        }
        for (EventListener<E> listener : listeners) {
            listener.handle(event, context);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <E extends IEvent> Collection<EventListener<E>> collectListeners(E event) {
        IEventTarget<E> target = (IEventTarget<E>) event.makeTarget();
        Collection<?> listeners = this.events.get(target);
        return (Collection<EventListener<E>>) listeners;
    }
}
