package com.openmodloader.api.loader;

import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.loader.ModConstructionException;

public interface ILanguageAdapter {
    <T extends IModConfigurator> T constructConfigurator(Class<T> modClass) throws ModConstructionException;
}
