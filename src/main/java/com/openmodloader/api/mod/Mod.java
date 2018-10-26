package com.openmodloader.api.mod;

import com.openmodloader.api.mod.config.IModConfig;

public class Mod {
    private final ModMetadata metadata;
    private final IModConfig config;

    public Mod(ModMetadata metadata, IModConfig config) {
        this.metadata = metadata;
        this.config = config;
    }

    public ModMetadata getMetadata() {
        return metadata;
    }

    public IModConfig getConfig() {
        return config;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;

        return ((Mod) obj).metadata.equals(metadata);
    }

    @Override
    public int hashCode() {
        return this.metadata.hashCode();
    }
}
