package com.openmodloader.api.mod.config;

import net.minecraft.registry.IdRegistry;

public interface IRegistrationConfig {
    <T> void registerEntries(IdRegistry<T> registry);
}
