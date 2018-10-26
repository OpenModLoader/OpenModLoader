package com.openmodloader.api.mod;

import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.api.mod.config.SimpleModConfig;

public interface IMod extends IModConfigurator {
    default IModData getData() {
        return ModAnnotationData.of(getClass());
    }

    default IModConfig initConfig() {
        return new SimpleModConfig();
    }
}
