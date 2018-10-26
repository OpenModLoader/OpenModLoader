package com.openmodloader.api.event;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventMap {
    private final ImmutableMap<IEventTarget<?>, Collection<IEventListener<?>>> events;

    private EventMap(ImmutableMap<IEventTarget<?>, Collection<IEventListener<?>>> events) {
        this.events = events;
    }

    @SuppressWarnings("unchecked")
    public <E extends IEvent> Collection<IEventListener<E>> getListeners(IEventTarget<E> target) {
        Collection<?> listeners = this.events.get(target);
        if (listeners == null) {
            return Collections.emptyList();
        }
        return (Collection<IEventListener<E>>) listeners;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<IEventTarget<?>, Collection<IEventListener<?>>> events = new HashMap<>();

        private Builder() {
        }

        public <E extends IEvent> Builder put(IEventTarget<E> target, IEventListener<E> listener) {
            Collection<IEventListener<?>> listeners = this.events.computeIfAbsent(target, t -> new ArrayList<>());
            listeners.add(listener);
            return this;
        }

        public EventMap build() {
            return new EventMap(ImmutableMap.copyOf(this.events));
        }
    }
}
