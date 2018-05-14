package com.openmodloader.loader.language;

import com.openmodloader.api.loader.language.ILanguageAdapter;

import java.lang.reflect.Constructor;

public class JavaLanguageAdapter implements ILanguageAdapter {
    @Override
    public Object createModInstance(Class<?> modClass) {
        try {
            Constructor<?> constructor = modClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
