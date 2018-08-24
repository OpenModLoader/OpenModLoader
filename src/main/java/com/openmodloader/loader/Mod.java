package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;

public interface Mod {

    String getModId();

    String getName();

    Version getVersion();

    ModContainer getContainer();

    boolean isEnabled();

    interface Blueprint {
        ModContainer getContainer();

        String getModId();

        String getName();

        Version getVersion();
    }
}
