package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.api.DataHandler;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.api.loader.SideHandler;
import com.openmodloader.core.data.JsonDataHandler;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.registry.RegistryEvent;
import com.openmodloader.loader.event.EventHandler;
import com.openmodloader.loader.event.LoadEvent;
import com.openmodloader.loader.json.SideTypeAdapter;
import com.openmodloader.loader.json.VersionTypeAdapter;
import com.openmodloader.loader.locator.ModLoader;
import com.openmodloader.network.test.TestPackets;
import net.fabricmc.api.Side;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.sound.Sound;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class OpenModLoader {
    public static final EventBus EVENT_BUS = new EventBus();
    public static final EventBus LOAD_BUS = new EventBus();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Version.class, new VersionTypeAdapter())
            .registerTypeAdapter(Side.class, new SideTypeAdapter())
            .setPrettyPrinting()
            .create();
    protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");
    private static DataHandler JSON_DATA_HANDLER = new JsonDataHandler();
    private static boolean initialized = false;
    private static SideHandler sideHandler;
    private static File gameDir;
    private static File configDir;
    private static File modsDir;
    private static File librariesDir;
    private static EventPhase currentPhase;
    private static Mod activeMod;

    private OpenModLoader() {
    }

    public static EventPhase getCurrentPhase() {
        return currentPhase;
    }

    public static void setCurrentPhase(EventPhase currentPhase) {
        OpenModLoader.currentPhase = currentPhase;
    }

    public static Side getCurrentSide() {
        return getSideHandler().getSide();
    }

    public static Mod getActiveMod() {
        return activeMod;
    }

    public static void setActiveMod(Mod info) {
        activeMod = info;
    }

    public static Gson getGson() {
        return GSON;
    }

    public static DataHandler getJsonDataHandler() {
        return JSON_DATA_HANDLER;
    }

    public static void initialize(File runDirectory, SideHandler sideHandler) throws IOException {
        OpenModLoader.sideHandler = sideHandler;
        LOGGER.info("Starting OpenModLoader on " + sideHandler.getSide());
        if (initialized) {
            throw new RuntimeException("OpenModLoader has already been initialized!");
        }
        gameDir = runDirectory;
        configDir = new File(gameDir, "config");
        if (!configDir.exists())
            configDir.mkdirs();
        modsDir = new File(gameDir, "mods");
        if (!modsDir.exists())
            modsDir.mkdirs();
        librariesDir = new File(gameDir, "libraries");
        if (!librariesDir.exists())
            librariesDir.mkdirs();
        ModLoader.loadMods();
        initialized = true;

        EventHandler handler = new EventHandler();
        EVENT_BUS.register(handler);
        LOAD_BUS.register(handler);
        LOAD_BUS.post(new LoadEvent.Construction());
        LOAD_BUS.post(new RegistryEvent<>(Item.REGISTRY, Item.class));
        LOAD_BUS.post(new RegistryEvent<>(Block.REGISTRY, Block.class));
        LOAD_BUS.post(new RegistryEvent<>(Fluid.REGISTRY, Fluid.class));
        LOAD_BUS.post(new RegistryEvent<>(Biome.REGISTRY, Biome.class));
        LOAD_BUS.post(new RegistryEvent<>(Enchantment.REGISTRY, Enchantment.class));
        LOAD_BUS.post(new RegistryEvent<>(Potion.REGISTRY, Potion.class));
        LOAD_BUS.post(new RegistryEvent<>(Sound.REGISTRY, Sound.class));
        Block.REGISTRY.forEach(block -> block.getStateContainer().getValidStates().stream().filter(
                state -> Block.STATE_IDS.getId(state) == -1
        ).forEach(Block.STATE_IDS::add));
        TestPackets.load();
        LOAD_BUS.post(new LoadEvent.Finalization());
    }

    public static SideHandler getSideHandler() {
        return sideHandler;
    }

    public static Version getVersion() {
        return ModLoader.getModInstance("openmodloader")
                .map(Mod::getVersion)
                .orElseGet(() -> Version.forIntegers(0, 0, 0));
    }

    public static File getGameDirectory() {
        return gameDir;
    }
}