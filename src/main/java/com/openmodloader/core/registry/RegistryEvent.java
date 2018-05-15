package com.openmodloader.core.registry;

import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.loader.OpenModLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.IRegistry;

import java.lang.reflect.Type;

public class RegistryEvent<T> implements Event.Generic, Event.PhaseLimit {
    private final IRegistry<Identifier, T> registry;
    private final Class<T> type;

    public RegistryEvent(IRegistry<Identifier, T> registry, Class<T> type) {
        this.registry = registry;
        this.type = type;
    }

    @Override
    public EventPhase[] getPossiblePhases() {
        return new EventPhase[]{EventPhase.DEFAULT, EventPhase.POST};
    }

    @Override
    public boolean matchesGenericType(Class<? extends Generic> eventType, int index, Type type) {
        return this.type == type;
    }

    public void register(String name, T value) {
        if (!name.contains(":"))
            name = String.format("%s:%s", OpenModLoader.getActiveMod().getModId(), name);
        register(new Identifier(name), value);
    }

    public void register(Identifier identifier, T value) {
        registry.put(identifier, value);
    }
}