package com.openmodloader.api.mod;

import com.github.zafarkhaja.semver.Version;

public interface IModData {
    String getModId();
    Version getVersion();
    String[] getDependencies();
}
