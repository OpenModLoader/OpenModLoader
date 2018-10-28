package com.openmodloader.loader;

import com.openmodloader.api.IOmlContext;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.Mod;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class OmlBootstrap {
    private static final Logger LOGGER = LogManager.getLogger(OmlBootstrap.class);

    private final File gameDir;
    private final File configDir;
    private final File modsDir;
    private final File librariesDir;

    private final List<IModReporter> modReporters = new ArrayList<>();

    public OmlBootstrap() {
        addModReporter(new BuiltinModReporter());
        addModReporter(new DevModReporter());

        gameDir = OpenModLoader.getContext().getRunDirectory();
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

    public void addModReporter(IModReporter reporter) {
        this.modReporters.add(reporter);
    }

    public OpenModLoader create() {
        IOmlContext context = OpenModLoader.getContext();
        LOGGER.info("Bootstrapping OpenModLoader on " + context.getPhysicalSide());

        ModList modList = new ModList(collectMods());
        return new OpenModLoader(modList);
    }

    private Collection<Mod> collectMods() {
        ModReportCollector reportCollector = new ModReportCollector();
        for (IModReporter reporter : modReporters) {
            reporter.apply(reportCollector);
        }

        Collection<ModReportCollector.Report> reports = reportCollector.getReports();
        LOGGER.info("Collected {} reported mods", reports.size());

        return reports.stream()
                .map(this::constructMod)
                .collect(Collectors.toList());
    }

    private Mod constructMod(ModReportCollector.Report report) {
        ModMetadata metadata = report.getMetadata();
        IModConfigurator configurator = report.getConfigurator();

        IModConfig config = configurator.initConfig();
        configurator.configure(config);

        return new Mod(metadata, config);
    }
}
