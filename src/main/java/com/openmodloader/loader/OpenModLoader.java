package com.openmodloader.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.api.loader.SideHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public final class OpenModLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static boolean initialized = false;
    private static SideHandler sideHandler;
    private static File gameDir;
    private static File configDir;
    private static File modsDir;
    private static List<ModContainer> CONTAINERS = new ArrayList<>();

    private OpenModLoader(SideHandler sideHandler) {
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void initialize(File runDirectory, SideHandler sideHandler) throws IOException {
        OpenModLoader.sideHandler = sideHandler;
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
        loadMods();
        initialized = true;
    }

    private static void loadMods() throws IOException {
        for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
            JarFile jar = new JarFile(file);
            ModInfo info = ModInfo.readFromJar(jar);
            if (info == null)
                continue;
            CONTAINERS.add(new ModContainer(jar, info));
        }
    }
}