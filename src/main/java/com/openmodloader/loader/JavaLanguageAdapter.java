package com.openmodloader.loader;

import com.openmodloader.api.loader.ILanguageAdapter;
import com.openmodloader.api.mod.config.IModConfigurator;

public final class JavaLanguageAdapter implements ILanguageAdapter {
    public static final JavaLanguageAdapter INSTANCE = new JavaLanguageAdapter();

    private JavaLanguageAdapter() {
    }

    @Override
    public <T extends IModConfigurator> T constructConfigurator(Class<T> modClass) throws ModConstructionException {
        try {
            return modClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ModConstructionException(e);
        }
    }
}
