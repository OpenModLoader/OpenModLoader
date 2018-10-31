package com.openmodloader.api.mod.config;

import com.openmodloader.api.event.EventMap;

public interface IEventConfig {
    void applyTo(EventMap.Builder builder);
}
