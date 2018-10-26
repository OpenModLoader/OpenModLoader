package com.openmodloader.loader;

import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.Mod;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collection;

public final class OpenModLoader {
    protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");

    private static OpenModLoader instance;

    private final OMLContext context;

    private File gameDir;
    private File configDir;
    private File modsDir;
    private File librariesDir;

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

    public void loadMods() {
        ModReportCollector reportCollector = new ModReportCollector();

        IModReporter[] reporters = new IModReporter[] { new BuiltinModReporter() };
        for (IModReporter reporter : reporters) {
            reporter.apply(reportCollector);
        }

        Collection<ModReportCollector.Report> reports = reportCollector.getReports();
        for (ModReportCollector.Report report : reports) {
            ModMetadata metadata = report.getMetadata();
            IModConfigurator configurator = report.getConfigurator();

            IModConfig config = configurator.initConfig();
            configurator.configure(config);

            Mod mod = new Mod(metadata, config);
        }

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
}
