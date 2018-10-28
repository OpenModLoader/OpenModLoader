package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.api.IOmlContext;
import com.openmodloader.api.mod.config.IRegistrationConfig;
import com.openmodloader.core.event.EventDispatcher;
import net.minecraft.registry.IdRegistry;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class OpenModLoader {
    public static final Version VERSION = Version.valueOf("1.0.0");

    private static Logger LOGGER = LogManager.getFormatterLogger(OpenModLoader.class);

    private static OpenModLoader instance;
    private static IOmlContext context;

    private final ModList modList;
    private final EventDispatcher eventDispatcher;

    OpenModLoader(ModList modList) {
        this.modList = modList;
        this.eventDispatcher = modList.buildEventDispatcher();
    }

    public static void offerContext(IOmlContext context) {
        if (OpenModLoader.context != null) {
            throw new IllegalStateException("OmlContext has already been initialized");
        }
        OpenModLoader.context = context;
    }

    public static void offerInstance(OpenModLoader instance) {
        if (OpenModLoader.instance != null) {
            throw new IllegalStateException("OpenModLoader has already been initialized!");
        }
        OpenModLoader.instance = instance;
    }

    public static OpenModLoader get() {
        if (instance == null) {
            throw new IllegalStateException("OpenModLoader not yet initialized!");
        }
        return instance;
    }

    public static IOmlContext getContext() {
        return context;
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public ModList getModList() {
        return modList;
    }

    public void initialize() {
        offerInstance(this);

        for (Registry<?> registry : Registry.REGISTRIES) {
            if (registry instanceof IdRegistry) {
                initializeRegistry((IdRegistry<?>) registry);
            }
        }
    }

    private <T> void initializeRegistry(IdRegistry<T> registry) {
        for (IRegistrationConfig config : modList.getRegistrationConfigs()) {
            config.registerEntries(registry);
        }
    }
}
