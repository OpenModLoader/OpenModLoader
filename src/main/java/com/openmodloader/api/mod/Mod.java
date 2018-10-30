package com.openmodloader.api.mod;

import com.openmodloader.api.mod.config.IModConfig;

public class Mod {
    private final ModMetadata metadata;
    private final IModConfig config;
    private final boolean global;

    public Mod(ModMetadata metadata, IModConfig config, boolean global) {
        this.metadata = metadata;
        this.config = config;
        this.global = global;
    }

    public ModMetadata getMetadata() {
        return metadata;
    }

    public IModConfig getConfig() {
        return config;
    }

    public boolean isGlobal() {
        return global;
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
