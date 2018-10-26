package com.openmodloader.api.mod.config;

import java.util.Collection;

public interface IModConfig {
    default void apply(IModConfigurator configurator) {
        configurator.configure(this);
    }

    void addEventConfig(IEventConfig config);

    void addRegistrationConfig(IRegistrationConfig config);

    Collection<IEventConfig> getEventConfigs();

    Collection<IRegistrationConfig> getRegistrationConfigs();
}
