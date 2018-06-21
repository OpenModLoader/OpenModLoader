package com.openmodloader.api.registry;

import com.openmodloader.api.data.DataObject;
import net.minecraft.util.Identifier;

public interface IRegistryEntry<V> {
    default void setRegistryName(String name) {
        setRegistryName(new Identifier(name));
    }

    default void setRegistryName(String domain, String path) {
        setRegistryName(new Identifier(domain, path));
    }

    default DataObject<V> asDataObject() {
        return DataObject.of((V) this);
    }

    Identifier getRegistryName();

    void setRegistryName(Identifier identifier);
}