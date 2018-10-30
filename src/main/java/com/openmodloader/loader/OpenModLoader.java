package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.openmodloader.api.IGameContext;
import com.openmodloader.api.event.IEventDispatcher;
import com.openmodloader.api.loader.ILanguageAdapter;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.Mod;
import com.openmodloader.api.mod.ModCandidate;
import com.openmodloader.api.mod.config.IRegistrationConfig;
import com.openmodloader.core.event.VoidEventDispatcher;
import net.minecraft.registry.IdRegistry;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Handling non-global mods, specific to world instances
public final class OpenModLoader {
    public static final Version VERSION = Version.valueOf("1.0.0");

    private static Logger LOGGER = LogManager.getFormatterLogger(OpenModLoader.class);

    private final ImmutableList<IModReporter> modReporters;
    private final ImmutableMap<String, ILanguageAdapter> languageAdapters;

    private static OpenModLoader instance;
    private static IGameContext context;

    private IEventDispatcher eventDispatcher = VoidEventDispatcher.INSTANCE;

    private ModContext installedModContext;
    private ModContext modContext;

    OpenModLoader(ImmutableList<IModReporter> modReporters, ImmutableMap<String, ILanguageAdapter> languageAdapters) {
        this.modReporters = modReporters;
        this.languageAdapters = languageAdapters;
    }

    public static void offerContext(IGameContext context) {
        if (OpenModLoader.context != null) {
            throw new IllegalStateException("OmlContext has already been initialized");
        }
        OpenModLoader.context = context;
    }

    public static void offerInstance(OpenModLoader instance) {
        if (OpenModLoader.instance != null) {
            throw new IllegalStateException("OpenModLoader has already been initialized!");
        }
        OpenModLoader.instance = instance;
    }

    public static OpenModLoader get() {
        if (instance == null) {
            throw new IllegalStateException("OpenModLoader not yet initialized!");
        }
        return instance;
    }

    public static IGameContext getContext() {
        return context;
    }

    public IEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    @Nullable
    public ModContext getInstalledModContext() {
        return installedModContext;
    }

    @Nullable
    public ModContext getModContext() {
        return modContext;
    }

    public void initialize() {
        offerInstance(this);

        Collection<ModCandidate> installedCandidates = collectModCandidates();
        LOGGER.info("Collected {} mod candidates", installedCandidates.size());

        List<Mod> installedMods = installedCandidates.stream()
                .map(ModCandidate::construct)
                .collect(Collectors.toList());

        List<Mod> globalMods = installedMods.stream()
                .filter(Mod::isGlobal)
                .collect(Collectors.toList());

        installedModContext = new ModContext(installedMods);
        modContext = new ModContext(globalMods);

        eventDispatcher = modContext.buildEventDispatcher();

        for (Registry<?> registry : Registry.REGISTRIES) {
            if (registry instanceof IdRegistry) {
                initializeRegistry((IdRegistry<?>) registry);
            }
        }
    }

    private Collection<ModCandidate> collectModCandidates() {
        ModReportCollector reportCollector = new ModReportCollector();
        ModConstructor constructor = new ModConstructor(this.languageAdapters);

        for (IModReporter reporter : modReporters) {
            reporter.apply(reportCollector, constructor);
        }

        return reportCollector.getCandidates();
    }

    private <T> void initializeRegistry(IdRegistry<T> registry) {
        for (IRegistrationConfig config : installedModContext.getRegistrationConfigs()) {
            config.registerEntries(registry);
        }
    }
}
