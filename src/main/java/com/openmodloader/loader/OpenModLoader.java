package com.openmodloader.loader;

import com.openmodloader.api.event.EventMap;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.Mod;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IEventConfig;
import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.core.event.EventDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public final class OpenModLoader {
    protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");

    private static OpenModLoader instance;

    private final OMLContext context;

    private final File gameDir;
    private final File configDir;
    private final File modsDir;
    private final File librariesDir;

    private EventDispatcher eventDispatcher;

    private OpenModLoader(OMLContext context) {
        this.context = context;

        gameDir = context.getRunDirectory();
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
    }

    public static OpenModLoader initialize(OMLContext context) {
        if (instance != null) {
            throw new IllegalStateException("OpenModLoader has already been initialized!");
        }

        LOGGER.info("Initializing OpenModLoader on " + context.getPhysicalSide());

        instance = new OpenModLoader(context);
        return instance;
    }

    public static OpenModLoader get() {
        if (instance == null) {
            throw new IllegalStateException("OpenModLoader not yet initialized!");
        }
        return instance;
    }

    public OMLContext getContext() {
        return context;
    }

    // TODO: Should we have a separate object that's initialized only once mods are loaded?
    public EventDispatcher getEventDispatcher() {
        if (eventDispatcher == null) {
            throw new IllegalStateException("Event dispatcher not yet initialized");
        }
        return eventDispatcher;
    }

    public void loadMods() {
        ModReportCollector reportCollector = new ModReportCollector();

        IModReporter[] reporters = new IModReporter[] { new BuiltinModReporter() };
        for (IModReporter reporter : reporters) {
            reporter.apply(reportCollector);
        }

        Collection<Mod> mods = reportCollector.getReports().stream()
                .map(this::constructMod)
                .collect(Collectors.toList());

        EventMap.Builder eventBuilder = EventMap.builder();
        for (Mod mod : mods) {
            Collection<IEventConfig> eventConfigs = mod.getConfig().getEventConfigs();
            for (IEventConfig config : eventConfigs) {
                config.applyTo(eventBuilder);
            }
        }

        eventDispatcher = new EventDispatcher(eventBuilder.build());

        /*ServiceLoader<IModConfigurator> modServiceLoader = ServiceLoader.load(IModConfigurator.class);

        List<Mod> mods = new ArrayList<>();

        mods.add(new Mod(metadata, new InjectedMod("openmodloader", Version.valueOf("1.0.0"))));
        mods.add(new Mod(metadata, new InjectedMod("minecraft", Version.valueOf("1.14.0+18w43b"))));

        for (File file : FileUtils.listFiles(modsDir, new String[] { "jar" }, true)) {
            ModClassLoader loader = new ModClassLoader(new URL[] { file.toURI().toURL() });
            ServiceLoader<IModConfigurator> jarServiceLoader = ServiceLoader.load(IModConfigurator.class, loader);
            jarServiceLoader.forEach(mod -> mods.add(new Mod(metadata, mod)));
        }

        modServiceLoader.forEach(mod -> mods.add(new Mod(metadata, mod)));

        mods.forEach(mod -> modMap.put(mod.getData().getModId(), mod));*/
    }

    private Mod constructMod(ModReportCollector.Report report) {
        ModMetadata metadata = report.getMetadata();
        IModConfigurator configurator = report.getConfigurator();

        IModConfig config = configurator.initConfig();
        configurator.configure(config);

        return new Mod(metadata, config);
    }
}
