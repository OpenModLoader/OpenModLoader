package com.openmodloader.api.mod.config;

import com.google.common.collect.ImmutableList;
import com.openmodloader.api.event.EventMap;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventListener;
import com.openmodloader.api.event.IEventTarget;

public class SimpleEventConfig implements IEventConfig {
    private final ImmutableList<Listener<?>> listeners;

    private SimpleEventConfig(ImmutableList<Listener<?>> listeners) {
        this.listeners = listeners;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void applyTo(EventMap.Builder builder) {
        for (Listener<?> listener : listeners) {
            putListener(builder, listener);
        }
    }

    private <E extends IEvent> void putListener(EventMap.Builder builder, Listener<E> listener) {
        builder.put(listener.target, listener.listener);
    }

    public static class Builder {
        private final ImmutableList.Builder<Listener<?>> listeners = ImmutableList.builder();

        private Builder() {
        }

        public <E extends IEvent> Builder listen(IEventTarget<E> target, IEventListener<E> listener) {
            listeners.add(new Listener<>(target, listener));
            return this;
        }

        public SimpleEventConfig build() {
            return new SimpleEventConfig(listeners.build());
        }
    }

    private static class Listener<E extends IEvent> {
        private final IEventTarget<E> target;
        private final IEventListener<E> listener;

        private Listener(IEventTarget<E> target, IEventListener<E> listener) {
            this.target = target;
            this.listener = listener;
        }
    }
}
