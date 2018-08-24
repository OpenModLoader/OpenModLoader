package com.openmodloader.loader;

import com.openmodloader.api.DataObject;

import java.nio.file.Path;

public class JarModContainer implements ModContainer {
    private final Path file;
    private final DataObject meta;

    public JarModContainer(Path file, DataObject meta) {
        this.file = file;
        this.meta = meta;
    }

    @Override
    public Path getPath() {
        return file;
    }

    @Override
    public DataObject getMetadata() {
        return meta;
    }
}
