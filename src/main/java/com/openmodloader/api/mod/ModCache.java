package com.openmodloader.api.mod;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ModCache {
    private final IMod mod;
    private final IModData data;

    public ModCache(IMod mod) {
        this.mod = mod;
        this.data = mod.getData();
    }

    public IMod getMod() {
        return mod;
    }

    public IModData getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ModCache modCache = (ModCache) o;

        return new EqualsBuilder()
                .append(data.getModId(), modCache.data.getModId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(data.getModId())
                .toHashCode();
    }
}