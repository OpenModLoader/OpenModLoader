package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.api.loader.SideHandler;
import com.openmodloader.api.loader.language.ILanguageAdapter;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.registry.RegistryEvent;
import com.openmodloader.loader.event.EventHandler;
import com.openmodloader.loader.event.LoadEvent;
import com.openmodloader.loader.exception.MissingModsException;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.biome.Biome;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;

public final class OpenModLoader {
    public static final EventBus EVENT_BUS = new EventBus();
    public static final EventBus LOAD_BUS = new EventBus();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");
    private static boolean initialized = false;
    private static SideHandler sideHandler;
    private static File gameDir;
    private static File configDir;
    private static File modsDir;
    private static File modulesDir;
    private static EventPhase currentPhase;
    private static Map<String, ModInfo> MOD_INFO_MAP = new HashMap<>();
    private static Map<String, ModContainer> MOD_CONTAINER_MAP = new HashMap<>();
    private static Map<String, ILanguageAdapter> LANGUAGE_ADAPTERS = new HashMap<>();
    private static ModInfo activeMod;

    private OpenModLoader() {
    }

    public static EventPhase getCurrentPhase() {
        return currentPhase;
    }

    public static void setCurrentPhase(EventPhase currentPhase) {
        OpenModLoader.currentPhase = currentPhase;
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
        modulesDir = new File(gameDir, "modules");
        if (!modulesDir.exists())
            modulesDir.mkdirs();
        loadMods();
        loadLibraries();
        scanDependencies();
        initialized = true;

        EventHandler handler = new EventHandler();
        EVENT_BUS.register(handler);
        LOAD_BUS.register(handler);
        LOAD_BUS.post(new LoadEvent.BusRegistration());
        LOAD_BUS.post(new RegistryEvent<>(Block.REGISTRY, Block.class));
        LOAD_BUS.post(new RegistryEvent<>(Item.REGISTRY, Item.class));
        LOAD_BUS.post(new RegistryEvent<>(Biome.REGISTRY, Biome.class));
    }

    private static void scanDependencies() {
        Map<String, List<String>> missingMods = new HashMap<>();
        Map<String, List<String>> wrongVersionMods = new HashMap<>();
        for (ModInfo info : MOD_INFO_MAP.values()) {
            for (String depend : info.getDependencies()) {
                String[] split = depend.split("@");
                ModInfo dependInfo = getModInfo(split[0]);
                if (dependInfo == null) {
                    if (!missingMods.containsKey(info.getModId()))
                        missingMods.put(info.getModId(), new ArrayList<>());
                    missingMods.get(info.getModId()).add(depend);
                    continue;
                }
                if (split.length > 1) {
                    Version dependVersion = Version.valueOf(dependInfo.getVersion());
                    if (!dependVersion.satisfies(split[1])) {
                        if (!wrongVersionMods.containsKey(info.getModId()))
                            wrongVersionMods.put(info.getModId(), new ArrayList<>());
                        wrongVersionMods.get(info.getModId()).add(depend);
                    }
                }
            }
            List<String> missing = missingMods.get(info.getModId());
            List<String> wrongVersion = wrongVersionMods.get(info.getModId());
            if ((missing != null && !missing.isEmpty()) || (wrongVersion != null && !wrongVersion.isEmpty())) {
                MOD_INFO_MAP.remove(info.getModId());
                MOD_CONTAINER_MAP.remove(info.getModId());
            }
        }
        if (!missingMods.isEmpty() || !wrongVersionMods.isEmpty())
            throw new MissingModsException(missingMods, wrongVersionMods);
    }

    private static void loadLibraries() throws IOException {

    }

    private static List<ModInfo> locateClasspathMods(File dir) {
        List<ModInfo> mods = new ArrayList<>();
        String javaHome = System.getProperty("java.home");

        URL[] urls = Launch.classLoader.getURLs();
        for (URL url : urls) {
            if (url.getPath().startsWith(javaHome) || url.getPath().startsWith(dir.getAbsolutePath())) {
                continue;
            }
            LOGGER.debug("Attempting to find classpath mods from " + url.getPath());

            File f = new File(url.getFile());
            if (f.exists()) {
                if (f.isDirectory()) {
                    File modJson = new File(f, "mod.json");
                    if (modJson.exists()) {
                        try {
                            mods.addAll(Arrays.asList(ModInfo.readFromFile(modJson)));
                        } catch (FileNotFoundException e) {
                            LOGGER.error("Unable to load mod from directory " + f.getPath());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return mods;
    }

    private static void loadMods() throws IOException {
        for (ModInfo info : locateClasspathMods(modsDir)) {
            MOD_INFO_MAP.put(info.getModId(), info);
            if (info.getMainClass().isEmpty()) {
                continue;
            }
            try {
                if (!LANGUAGE_ADAPTERS.containsKey(info.getLanguageAdapter()))
                    LANGUAGE_ADAPTERS.put(info.getLanguageAdapter(), (ILanguageAdapter) Class.forName(info.getLanguageAdapter()).getConstructor().newInstance());
                ModContainer container = new ModContainer(info, LANGUAGE_ADAPTERS.get(info.getLanguageAdapter()).createModInstance(Class.forName(info.getMainClass())));
                MOD_CONTAINER_MAP.put(info.getModId(), container);
                setActiveMod(info);
                LOAD_BUS.register(container.getModInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
            JarFile jar = new JarFile(file);
            ModInfo[] infos = ModInfo.readFromJar(jar);
            if (infos == null)
                continue;
            for (ModInfo info : infos) {
                MOD_INFO_MAP.put(info.getModId(), info);
                if (info.getMainClass().isEmpty()) {
                    continue;
                }
                try {
                    if (!LANGUAGE_ADAPTERS.containsKey(info.getLanguageAdapter()))
                        LANGUAGE_ADAPTERS.put(info.getLanguageAdapter(), (ILanguageAdapter) Class.forName(info.getLanguageAdapter()).getConstructor().newInstance());
                    ModContainer container = new ModContainer(info, LANGUAGE_ADAPTERS.get(info.getLanguageAdapter()).createModInstance(Class.forName(info.getMainClass())));
                    MOD_CONTAINER_MAP.put(info.getModId(), container);
                    setActiveMod(info);
                    LOAD_BUS.register(container.getModInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        setActiveMod(getModInfo("openmodloader"));
    }

    public static SideHandler getSideHandler() {
        return sideHandler;
    }

    public static ModInfo getModInfo(String modid) {
        return MOD_INFO_MAP.get(modid);
    }

    public static String getVersion() {
        return getModInfo("openmodloader").getVersion();
    }
}