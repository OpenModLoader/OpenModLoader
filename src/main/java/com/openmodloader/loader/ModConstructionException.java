package com.openmodloader.loader;

public class ModConstructionException extends Exception {
    public ModConstructionException() {
    }

    public ModConstructionException(String message) {
        super(message);
    }

    public ModConstructionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModConstructionException(Throwable cause) {
        super(cause);
    }
}
