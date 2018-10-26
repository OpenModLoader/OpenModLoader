package com.openmodloader.api.mod.config;

import com.google.common.collect.ImmutableList;
import com.openmodloader.api.event.EventListener;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventTarget;
import com.openmodloader.api.event.TargetedListener;

import java.util.Collection;

public class SimpleEventConfig implements IEventConfig {
    private final ImmutableList<TargetedListener<?>> listeners;

    private SimpleEventConfig(ImmutableList<TargetedListener<?>> listeners) {
        this.listeners = listeners;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Collection<TargetedListener<?>> collectListeners() {
        return this.listeners;
    }

    public static class Builder {
        private final ImmutableList.Builder<TargetedListener<?>> listeners = ImmutableList.builder();

        private Builder() {
        }

        public <E extends IEvent> Builder listen(IEventTarget<E> target, EventListener<E> listener) {
            listeners.add(new TargetedListener<>(target, listener));
            return this;
        }

        public SimpleEventConfig build() {
            return new SimpleEventConfig(listeners.build());
        }
    }
}
