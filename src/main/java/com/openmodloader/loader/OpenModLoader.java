package com.openmodloader.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.api.loader.SideHandler;
import com.openmodloader.core.EventBus;
import com.openmodloader.core.EventHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;

public final class OpenModLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static boolean initialized = false;
    private static SideHandler sideHandler;
    private static File gameDir;
    private static File configDir;
    private static File modsDir;
    private static List<ModInfo> MODS = new ArrayList<>();
    private static Map<String, ModInfo> MOD_INFO_MAP = new HashMap<>();
    private static ModInfo activeMod;
    private static ModInfo loaderInfo;
    public static final EventBus EVENT_BUS = new EventBus();

    public static ModInfo getActiveMod() {
        return activeMod;
    }

    public static void setActiveMod(ModInfo info) {
        activeMod = info;
    }

    private OpenModLoader() {
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void initialize(File runDirectory, SideHandler sideHandler) throws IOException {
        OpenModLoader.sideHandler = sideHandler;
        if (initialized) {
            throw new RuntimeException("OpenModLoader has already been initialized!");
        }
        loaderInfo = new ModInfo("openmodloader");
        setActiveMod(loaderInfo);
        gameDir = runDirectory;
        configDir = new File(gameDir, "config");
        if (!configDir.exists())
            configDir.mkdirs();
        modsDir = new File(gameDir, "mods");
        if (!modsDir.exists())
            modsDir.mkdirs();
        loadMods();
        initialized = true;
    }

    private static void loadMods() throws IOException {
        for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
            JarFile jar = new JarFile(file);
            ModInfo[] infos = ModInfo.readFromJar(jar);
            if (infos == null)
                continue;
            MODS.addAll(Arrays.asList(infos));
            for (ModInfo info : infos)
                MOD_INFO_MAP.put(info.getModId(), info);
        }
    }

    public static SideHandler getSideHandler() {
        return sideHandler;
    }

    public static ModInfo getModInfo(String modid) {
        return MOD_INFO_MAP.get(modid);
    }
}