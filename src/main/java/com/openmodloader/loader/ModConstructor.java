package com.openmodloader.loader;

import com.openmodloader.api.loader.ILanguageAdapter;
import com.openmodloader.api.mod.config.IModConfigurator;

import java.util.Map;

public class ModConstructor {
    private final Map<String, ILanguageAdapter> languageAdapters;

    public ModConstructor(Map<String, ILanguageAdapter> languageAdapters) {
        this.languageAdapters = languageAdapters;
    }

    public <T extends IModConfigurator> T constructConfigurator(Class<T> configuratorClass, String adapterType) throws ModConstructionException {
        ILanguageAdapter adapter = this.languageAdapters.getOrDefault(adapterType, JavaLanguageAdapter.INSTANCE);
        return adapter.constructConfigurator(configuratorClass);
    }
}
