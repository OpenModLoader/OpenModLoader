package com.openmodloader.core;

import com.openmodloader.api.event.Event;

import java.lang.reflect.Type;

public class RegistryEvent<T> implements Event.Generic {
    private Class<T> type;

    public RegistryEvent(Class<T> type) {
        this.type = type;
    }

    @Override
    public boolean matchesGenericType(Class<? extends Generic> eventType, int index, Type type) {
        return this.type == type;
    }
}
