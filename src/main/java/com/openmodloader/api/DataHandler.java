package com.openmodloader.api;

import java.io.InputStream;

public interface DataHandler {
    DataObject read(InputStream stream);
}
