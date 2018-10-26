package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openmodloader.TestMod;
import com.openmodloader.api.data.DataObject;
import com.openmodloader.api.event.TargetedListener;
import com.openmodloader.api.loader.SideHandler;
import com.openmodloader.api.mod.IMod;
import com.openmodloader.api.mod.IModData;
import com.openmodloader.api.mod.ModCache;
import com.openmodloader.api.mod.config.IEventConfig;
import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.core.event.EventDispatcher;
import com.openmodloader.core.event.PretendGuiEvent;
import com.openmodloader.loader.exception.MissingModsException;
import com.openmodloader.loader.json.SideTypeAdapter;
import com.openmodloader.loader.json.VersionTypeAdapter;
import com.openmodloader.network.test.TestPackets;
import net.fabricmc.api.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.menu.GuiMainMenu;
import net.minecraft.resource.IPackFinder;
import net.minecraft.resource.PackMetadata;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.pack.PhysicalResourcePack;
import net.minecraft.text.TextComponentString;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public final class OpenModLoader {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Version.class, new VersionTypeAdapter())
            .registerTypeAdapter(Side.class, new SideTypeAdapter())
            .setPrettyPrinting()
            .create();

    protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");
    private static boolean initialized = false;
    private static SideHandler sideHandler;
    private static File gameDir;
    private static File configDir;
    private static File modsDir;
    private static File librariesDir;
    private static Map<String, ModCache> MOD_MAP = new HashMap<>();
    private static IModData activeMod;

    private OpenModLoader() {
    }

    public static Side getCurrentSide() {
        return getSideHandler().getSide();
    }

    public static IModData getActiveMod() {
        return activeMod;
    }

    public static void setActiveMod(IModData mod) {
        ModCache cache = MOD_MAP.get(mod.getModId());
        activeMod = cache == null ? activeMod : cache.getData();
    }

    public static Set<String> getActiveModIds() {
        return ImmutableSet.copyOf(MOD_MAP.keySet());
    }

    public static Set<ModCache> getActiveMods() {
        return ImmutableSet.copyOf(MOD_MAP.values());
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
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        modsDir = new File(gameDir, "mods");
        if (!modsDir.exists()) {
            modsDir.mkdirs();
        }
        librariesDir = new File(gameDir, "libraries");
        if (!librariesDir.exists()) {
            librariesDir.mkdirs();
        }
        loadMods();
        scanDependencies();
        finalLoad();
        initialized = true;

        // TODO
//        EventHandler handler = new EventHandler();
//        EVENT_BUS.register(handler);
//        LOAD_BUS.register(handler);
//        LOAD_BUS.post(new LoadEvent.Construction());
//
//        LOAD_BUS.post(new RegistryEvent<>(Registry.ITEMS, Item.class));
//        LOAD_BUS.post(new RegistryEvent<>(Registry.BLOCKS, Block.class));
//        LOAD_BUS.post(new RegistryEvent<>(Registry.FLUIDS, Fluid.class));
//        LOAD_BUS.post(new RegistryEvent<>(Registry.BIOMES, Biome.class));
//        LOAD_BUS.post(new RegistryEvent<>(Registry.ENCHANTMENTS, Enchantment.class));
//        LOAD_BUS.post(new RegistryEvent<>(Registry.POTIONS, Potion.class));
//        LOAD_BUS.post(new RegistryEvent<>(Registry.SOUNDS, Sound.class));

//        IRegistry.BLOCKS.stream().forEach(block -> block.getStateContainer().getValidStates().stream().filter(
//                state -> Block.STATE_IDS.getId(state) == -1
//        ).forEach(Block.STATE_IDS::add));
        TestPackets.load();

        // TODO
//        LOAD_BUS.post(new LoadEvent.Finalization());
    }

    private static void testLoadMod() {
        TestMod mod = new TestMod();

        IModConfig config = mod.initConfig();
        mod.configure(config);

        Collection<IEventConfig> eventConfigs = config.getEventConfigs();
        List<TargetedListener<?>> listeners = eventConfigs.stream()
                .flatMap(c -> c.collectListeners().stream())
                .collect(Collectors.toList());

        EventDispatcher dispatcher = EventDispatcher.from(listeners);
        dispatcher.dispatch(new PretendGuiEvent<>(new GuiMainMenu()));
    }

    private static void finalLoad() {
        //getActiveMods().forEach(OpenModLoader::loadMod);
        Map<ModCache, PhysicalResourcePack> resourcePacks = new HashMap<>();
        /*getActiveMods().forEach(info -> {
            if (info.getData().getModId().equals("minecraft"))
                return;
            }
            File origin = info.getOrigin();
            if (origin.isDirectory()) {
                resourcePacks.put(info, new ModFolderPack(origin, info));
            } else {
                resourcePacks.put(info, new ModFilePack(origin, info));
            }
        });*/
        if (getSideHandler().getSide() == Side.CLIENT) {
            Minecraft.getInstance().getResourcePacks().addPackFinder(new IPackFinder() {
                @Override
                public <T extends ResourcePackInfo> void locateResourcePacks(Map<String, T> map, ResourcePackInfo.IFactory<T> iFactory) {
                    resourcePacks.forEach((info, pack) -> {
                        PackMetadata metadata;
                        try {
                            metadata = pack.getPackMetadata(PackMetadata.DESERIALISER);
                        } catch (IOException e) {
                            metadata = new PackMetadata(new TextComponentString(pack.getName() + " Resources"), 3);
                        }
                        map.put(Preconditions.checkNotNull(pack.getName(), "Mod Resources Name"), iFactory.create(pack.getName(), true, () -> pack, pack, metadata, ResourcePackInfo.Priority.BOTTOM));
                    });
                }
            });
        }
    }

    private static void scanDependencies() {
        Map<String, List<String>> missingMods = new HashMap<>();
        Map<String, List<String>> wrongVersionMods = new HashMap<>();
        for (ModCache mod : getActiveMods()) {
            IModData info = mod.getData();
            for (String depend : info.getDependencies()) {
                String[] split = depend.split("@");
                getModData(split[0]).ifPresent(dependInfo -> {
                    if (dependInfo == null) {
                        if (!missingMods.containsKey(info.getModId())) {
                            missingMods.put(info.getModId(), new ArrayList<>());
                        }
                        missingMods.get(info.getModId()).add(depend);
                        return;
                    }
                    if (split.length > 1) {
                        if (!dependInfo.getVersion().satisfies(split[1])) {
                            if (!wrongVersionMods.containsKey(info.getModId())) {
                                wrongVersionMods.put(info.getModId(), new ArrayList<>());
                            }
                            wrongVersionMods.get(info.getModId()).add(depend);
                        }
                    }
                });
            }
            List<String> missing = missingMods.get(info.getModId());
            List<String> wrongVersion = wrongVersionMods.get(info.getModId());
            if ((missing != null && !missing.isEmpty()) || (wrongVersion != null && !wrongVersion.isEmpty())) {
                MOD_MAP.remove(info.getModId());
            }
        }
        if (!missingMods.isEmpty() || !wrongVersionMods.isEmpty()) {
            throw new MissingModsException(missingMods, wrongVersionMods);
        }
    }

    private static void loadMods() throws IOException {

        ServiceLoader<IMod> modServiceLoader = ServiceLoader.load(IMod.class);

        List<ModCache> mods = new ArrayList<>();

        mods.add(new ModCache(new InjectedMod("openmodloader", Version.valueOf("1.0.0"))));
        mods.add(new ModCache(new InjectedMod("minecraft", Version.valueOf("1.14.0+18w43b"))));

        for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
            ModClassLoader loader = new ModClassLoader(new URL[]{file.toURI().toURL()});
            ServiceLoader<IMod> jarServiceLoader = ServiceLoader.load(IMod.class, loader);
            jarServiceLoader.forEach(mod -> mods.add(new ModCache(mod)));
        }

        modServiceLoader.forEach(mod -> mods.add(new ModCache(mod)));

        mods.forEach(mod -> MOD_MAP.put(mod.getData().getModId(), mod));

        //locateClasspathMods().forEach(OpenModLoader::addInfo);
        getModData("openmodloader").ifPresent(OpenModLoader::setActiveMod).orElseThrows(() -> new RuntimeException("Missing OML mod mapping, this should never happen!"));
    }

    public static SideHandler getSideHandler() {
        return sideHandler;
    }

    public static DataObject<IModData> getModData(String modid) {
        return DataObject.of(MOD_MAP.get(modid)).map(ModCache::getData);
    }

    public static Version getVersion() {
        return getModData("openmodloader").map(IModData::getVersion).orElseThrows(() -> new RuntimeException("Missing OML mod mapping, this should never happen!"));
    }
}
