package com.openmodloader.loader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.openmodloader.api.IGameContext;
import com.openmodloader.api.loader.ILanguageAdapter;
import com.openmodloader.api.loader.IModReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LoaderBootstrap {
    private static final Logger LOGGER = LogManager.getLogger(LoaderBootstrap.class);

    private final File gameDir;
    private final File configDir;
    private final File modsDir;
    private final File librariesDir;

    private final Map<String, ILanguageAdapter> languageAdapters = new HashMap<>();
    private final ImmutableList.Builder<IModReporter> modReporters = ImmutableList.builder();

    public LoaderBootstrap() {
        addModReporter(new BuiltinModReporter());
        addModReporter(new ClasspathModReporter());
        addLanguageAdapter("java", JavaLanguageAdapter.INSTANCE);

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
        modReporters.add(reporter);
    }

    public void addLanguageAdapter(String key, ILanguageAdapter adapter) {
        if (languageAdapters.containsKey(key)) {
            throw new IllegalArgumentException("Language adapter '" + key + "' is already registered");
        }
        languageAdapters.put(key, adapter);
    }

    public OpenModLoader create() {
        IGameContext context = OpenModLoader.getContext();
        LOGGER.info("Bootstrapping OpenModLoader on " + context.getPhysicalSide());

        return new OpenModLoader(modReporters.build(), ImmutableMap.copyOf(languageAdapters));
    }
}
