package com.openmodloader.api.mod.config;

public interface IModConfigurator {
    default IModConfig initConfig() {
        return new SimpleModConfig();
    }

    void configure(IModConfig config);
}
