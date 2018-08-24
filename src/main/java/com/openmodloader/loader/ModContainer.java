package com.openmodloader.loader;

import com.openmodloader.api.DataObject;

import java.nio.file.Path;

public interface ModContainer {

    Path getPath();

    DataObject getMetadata();

}
