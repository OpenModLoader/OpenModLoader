package com.openmodloader.loader;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.api.loader.SideHandler;
import com.openmodloader.api.loader.language.ILanguageAdapter;
import com.openmodloader.core.EventBus;
import com.openmodloader.loader.event.EventHandler;
import com.openmodloader.loader.event.LoadEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

public final class OpenModLoader {
    public static final EventBus EVENT_BUS = new EventBus();
    public static final EventBus LOAD_BUS = new EventBus();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static boolean initialized = false;
    private static SideHandler sideHandler;
    private static File gameDir;
    private static File configDir;
    private static File modsDir;
    private static File modulesDir;
    private static Map<String, ModInfo> MOD_INFO_MAP = new HashMap<>();
    private static Map<String, ModContainer> MOD_CONTAINER_MAP = new HashMap<>();
    private static Map<String, ILanguageAdapter> LANGUAGE_ADAPTERS = new HashMap<>();
    private static ModInfo activeMod;
    private static ModInfo loaderInfo;

    private OpenModLoader() {
    }

    public static ModInfo getActiveMod() {
        return activeMod;
    }

    public static void setActiveMod(ModInfo info) {
        activeMod = info;
    }

    public static Set<String> getActiveModIds() {
        return ImmutableSet.copyOf(MOD_INFO_MAP.keySet());
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void initialize(File runDirectory, SideHandler sideHandler) throws IOException {
        OpenModLoader.sideHandler = sideHandler;
        if (initialized) {
            throw new RuntimeException("OpenModLoader has already been initialized!");
        }
        System.out.println("Initialization");
        loaderInfo = new ModInfo("openmodloader");
        MOD_INFO_MAP.put(loaderInfo.getModId(), loaderInfo);
        setActiveMod(loaderInfo);
        gameDir = runDirectory;
        configDir = new File(gameDir, "config");
        if (!configDir.exists())
            configDir.mkdirs();
        modsDir = new File(gameDir, "mods");
        if (!modsDir.exists())
            modsDir.mkdirs();
        modulesDir = new File(gameDir, "modules");
        if (!modulesDir.exists())
            modulesDir.mkdirs();
        loadMods();
        loadModules();
        initialized = true;

        EVENT_BUS.register(new EventHandler());
        LOAD_BUS.post(new LoadEvent.BusRegistration());
    }

    private static void loadModules() throws IOException {

    }

    private static void loadMods() throws IOException {
        for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
            JarFile jar = new JarFile(file);
            ModInfo[] infos = ModInfo.readFromJar(jar);
            if (infos == null)
                continue;
            for (ModInfo info : infos) {
                MOD_INFO_MAP.put(info.getModId(), info);
                try {
                    if (!LANGUAGE_ADAPTERS.containsKey(info.getLanguageAdapter()))
                        LANGUAGE_ADAPTERS.put(info.getLanguageAdapter(), (ILanguageAdapter) Class.forName(info.getLanguageAdapter()).getConstructor().newInstance());
                    ModContainer container = new ModContainer(info, LANGUAGE_ADAPTERS.get(info.getLanguageAdapter()).createModInstance(Class.forName(info.getMainClass())));
                    MOD_CONTAINER_MAP.put(info.getModId(), container);
                    LOAD_BUS.register(container);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static SideHandler getSideHandler() {
        return sideHandler;
    }

    public static ModInfo getModInfo(String modid) {
        return MOD_INFO_MAP.get(modid);
    }
}
