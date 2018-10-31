package com.openmodloader.api.mod;

import com.github.zafarkhaja.semver.Version;

public class ModMetadata {
    private final String id;
    private final Version version;

    public ModMetadata(String id, Version version) {
        this.id = id;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ModMetadata metadata = (ModMetadata) obj;
        return metadata.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
