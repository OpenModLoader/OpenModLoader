package com.openmodloader.api.mod.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleModConfig implements IModConfig {
    private final List<IEventConfig> eventConfigs = new ArrayList<>();
    private final List<IRegistrationConfig> registrationConfigs = new ArrayList<>();

    @Override
    public void addEventConfig(IEventConfig config) {
        this.eventConfigs.add(config);
    }

    @Override
    public void addRegistrationConfig(IRegistrationConfig config) {
        this.registrationConfigs.add(config);
    }

    @Override
    public Collection<IEventConfig> getEventConfigs() {
        return this.eventConfigs;
    }

    @Override
    public Collection<IRegistrationConfig> getRegistrationConfigs() {
        return this.registrationConfigs;
    }
}
