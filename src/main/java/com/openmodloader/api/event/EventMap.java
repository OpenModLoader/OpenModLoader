package com.openmodloader.api.event;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class EventMap {
    private final ImmutableMap<Class<?>, Collection<Listener<?>>> events;

    private EventMap(ImmutableMap<Class<?>, Collection<Listener<?>>> events) {
        this.events = events;
    }

    @SuppressWarnings("unchecked")
    public <E extends IEvent> Stream<IEventListener<E>> getListeners(IEventTarget<E> target) {
        Collection<Listener<?>> listeners = this.events.get(target.getType());
        if (listeners == null) {
            return Stream.empty();
        }

        return listeners.stream()
                .filter(l -> l.canReceive(target))
                .map(l -> (IEventListener<E>) l.listener);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<Class<?>, Collection<Listener<?>>> events = new HashMap<>();

        private Builder() {
        }

        public <E extends IEvent> Builder put(IEventTarget<E> target, IEventListener<E> listener) {
            Class<E> targetType = target.getType();
            Collection<Listener<?>> listeners = this.events.computeIfAbsent(targetType, t -> new ArrayList<>());
            listeners.add(new Listener<>(target, listener));
            return this;
        }

        public EventMap build() {
            return new EventMap(ImmutableMap.copyOf(this.events));
        }
    }

    private static class Listener<E extends IEvent> {
        private final IEventTarget<E> target;
        private final IEventListener<E> listener;

        private Listener(IEventTarget<E> target, IEventListener<E> listener) {
            this.target = target;
            this.listener = listener;
        }

        boolean canReceive(IEventTarget<?> target) {
            return this.target.canReceive(target);
        }
    }
}
