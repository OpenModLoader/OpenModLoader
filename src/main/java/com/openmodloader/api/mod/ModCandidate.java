package com.openmodloader.api.mod;

import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;

public class ModCandidate {
    private final ModMetadata metadata;
    private final IModConfigurator configurator;
    private boolean global;

    public ModCandidate(ModMetadata metadata, IModConfigurator configurator) {
        this.metadata = metadata;
        this.configurator = configurator;
    }

    public ModCandidate global() {
        global = true;
        return this;
    }

    public boolean isGlobal() {
        return global;
    }

    public Mod construct() {
        IModConfig config = configurator.initConfig();
        configurator.configure(config);

        return new Mod(metadata, config, global);
    }
}
