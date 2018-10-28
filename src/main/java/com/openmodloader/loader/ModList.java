package com.openmodloader.loader;

import com.openmodloader.api.event.EventMap;
import com.openmodloader.api.mod.Mod;
import com.openmodloader.api.mod.config.IEventConfig;
import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IRegistrationConfig;
import com.openmodloader.core.event.EventDispatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ModList {
    private final Collection<Mod> mods;

    private final Collection<IEventConfig> eventConfigs = new ArrayList<>();
    private final Collection<IRegistrationConfig> registrationConfigs = new ArrayList<>();

    public ModList(Collection<Mod> mods) {
        this.mods = mods;
        for (Mod mod : mods) {
            IModConfig config = mod.getConfig();
            eventConfigs.addAll(config.getEventConfigs());
            registrationConfigs.addAll(config.getRegistrationConfigs());
        }
    }

    public EventDispatcher buildEventDispatcher() {
        EventMap.Builder eventBuilder = EventMap.builder();
        for (IEventConfig config : eventConfigs) {
            config.applyTo(eventBuilder);
        }
        return new EventDispatcher(eventBuilder.build());
    }

    public Collection<Mod> getMods() {
        return Collections.unmodifiableCollection(mods);
    }

    public Collection<IEventConfig> getEventConfigs() {
        return Collections.unmodifiableCollection(eventConfigs);
    }

    public Collection<IRegistrationConfig> getRegistrationConfigs() {
        return Collections.unmodifiableCollection(registrationConfigs);
    }
}
