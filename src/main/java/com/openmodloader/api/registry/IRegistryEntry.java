package com.openmodloader.api.registry;

import net.minecraft.util.Identifier;

public interface IRegistryEntry<V> {
    default void setRegistryName(String name) {
        setRegistryName(new Identifier(name));
    }

    default void setRegistryName(String domain, String path) {
        setRegistryName(new Identifier(domain, path));
    }

    default DataElementImpl<V> asDataObject() {
        return DataElementImpl.of((V) this);
    }

    Identifier getRegistryName();

    void setRegistryName(Identifier identifier);
}