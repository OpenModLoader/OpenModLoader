package com.openmodloader.api.mod.config;

import com.openmodloader.api.event.TargetedListener;

import java.util.Collection;

public interface IEventConfig {
    Collection<TargetedListener<?>> collectListeners();
}
