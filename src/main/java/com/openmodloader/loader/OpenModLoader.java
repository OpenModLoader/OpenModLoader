package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.api.data.DataObject;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.api.loader.SideHandler;
import com.openmodloader.api.loader.language.ILanguageAdapter;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.registry.RegistryEvent;
import com.openmodloader.core.util.ArrayUtil;
import com.openmodloader.loader.event.EventHandler;
import com.openmodloader.loader.event.LoadEvent;
import com.openmodloader.loader.exception.MissingModsException;
import com.openmodloader.network.test.TestPackets;
import net.fabricmc.api.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.resource.IPackFinder;
import net.minecraft.resource.PackMetadata;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.pack.PhysicalResourcePack;
import net.minecraft.sound.Sound;
import net.minecraft.text.TextComponentString;
import net.minecraft.world.biome.Biome;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
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
    private static File librariesDir;
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

    public static Side getCurrentSide() {
        return getSideHandler().getSide();
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

    public static Set<ModInfo> getActiveMods() {
        return ImmutableSet.copyOf(MOD_INFO_MAP.values());
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
        librariesDir = new File(gameDir, "libraries");
        if (!librariesDir.exists())
            librariesDir.mkdirs();
        loadMods();
        loadLibraries();
        scanDependencies();
        finalLoad();
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

    private static void finalLoad() {
        getActiveMods().forEach(OpenModLoader::loadMod);
        Map<ModInfo, PhysicalResourcePack> resourcePacks = new HashMap<>();
        getActiveMods().forEach(info -> {
            if (info.getModId().equals("minecraft"))
                return;
            File origin = info.getOrigin();
            if (origin.isDirectory()) {
                resourcePacks.put(info, new ModFolderPack(origin, info));
            } else {
                resourcePacks.put(info, new ModFilePack(origin, info));
            }
        });
        if (getSideHandler().getSide() == Side.CLIENT) {
            Minecraft.getInstance().getResourcePacks().addPackFinder(new IPackFinder() {
                @Override
                public <T extends ResourcePackInfo> void locateResourcePacks(Map<String, T> map, ResourcePackInfo.IFactory<T> iFactory) {
                    resourcePacks.forEach((info, pack) -> {
                        PackMetadata metadata;
                        try {
                            metadata = pack.getPackMetadata(PackMetadata.DESERIALISER);
                        } catch (IOException e) {
                            metadata = new PackMetadata(new TextComponentString(pack.getName() + " Resources"), info.getAssetVersion());
                        }
                        map.put(Preconditions.checkNotNull(pack.getName(), "Mod Resources Name"), iFactory.create(pack.getName(), true, () -> pack, pack, metadata, ResourcePackInfo.Priority.BOTTOM));
                    });
                }
            });
        }
    }

    private static void loadLibraries() {
        for (ModInfo info : getActiveMods()) {
            ModInfo[] infos = downloadLibraries(info);
            ArrayUtil.forEach(infos, libInfo -> {
                addInfo(libInfo);
                downloadLibraries(libInfo);
            });
        }
    }

    public static ModInfo[] downloadLibraries(ModInfo info) {
        if (info.getLibraries().length == 0)
            return new ModInfo[0];
        //TODO: Scan and download libraries
        return new ModInfo[0];
    }

    private static void scanDependencies() {
        Map<String, List<String>> missingMods = new HashMap<>();
        Map<String, List<String>> wrongVersionMods = new HashMap<>();
        for (ModInfo info : getActiveMods()) {
            for (String depend : info.getDependencies()) {
                String[] split = depend.split("@");
                getModInfo(split[0]).ifPresent(dependInfo -> {
                    if (dependInfo == null) {
                        if (!missingMods.containsKey(info.getModId()))
                            missingMods.put(info.getModId(), new ArrayList<>());
                        missingMods.get(info.getModId()).add(depend);
                        return;
                    }
                    if (split.length > 1) {
                        if (!dependInfo.getVersion().satisfies(split[1])) {
                            if (!wrongVersionMods.containsKey(info.getModId()))
                                wrongVersionMods.put(info.getModId(), new ArrayList<>());
                            wrongVersionMods.get(info.getModId()).add(depend);
                        }
                    }
                });
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

    private static List<ModInfo> locateClasspathMods() {
        List<ModInfo> mods = new ArrayList<>();
        try {
            Enumeration<URL> urls = ClassLoader.getSystemResources("mod.json");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url.getProtocol().equals("jar")) {
                    String path = url.getFile();
                    URL jarPath = new URL(path.substring(0, path.indexOf("!/")));
                    File file;
                    try {
                        file = new File(jarPath.toURI());
                    } catch (URISyntaxException e) {
                        file = new File(jarPath.getPath());
                    }
                    JarFile jar = new JarFile(file);
                    ModInfo[] infos = ModInfo.readFromJar(jar);
                    if (infos == null)
                        continue;
                    File finalFile = file;
                    ArrayUtil.forEach(infos, info -> info.setOrigin(finalFile));
                    mods.addAll(Arrays.asList(infos));
                } else {
                    ModInfo[] infos = ModInfo.readFromFile(new File(url.getFile()));
                    ArrayUtil.forEach(infos, info -> info.setOrigin(new File(url.getFile()).getParentFile()));
                    mods.addAll(Arrays.asList(infos));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mods;
    }

    private static void loadMod(ModInfo info) {
        if (!info.getMainClass().isEmpty()) {
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

    private static void loadMods() throws IOException {
        locateClasspathMods().forEach(OpenModLoader::addInfo);
        for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
            JarFile jar = new JarFile(file);
            ModInfo[] infos = ModInfo.readFromJar(jar);
            if (infos == null)
                continue;
            ArrayUtil.forEach(infos, info -> info.setOrigin(file));
            ArrayUtil.forEach(infos, OpenModLoader::addInfo);
        }
        getModInfo("openmodloader").ifPresent(OpenModLoader::setActiveMod).orElseThrows(() -> new RuntimeException("Missing OML mod mapping, this should never happen!"));
    }

    private static void addInfo(ModInfo info) {
        MOD_INFO_MAP.put(info.getModId(), info);
    }

    public static SideHandler getSideHandler() {
        return sideHandler;
    }

    public static DataObject<ModInfo> getModInfo(String modid) {
        return DataObject.of(MOD_INFO_MAP.get(modid));
    }

    public static Version getVersion() {
        return getModInfo("openmodloader").map(ModInfo::getVersion).orElseThrows(() -> new RuntimeException("Missing OML mod mapping, this should never happen!"));
    }
}