package com.openmodloader.api.mod;

import com.openmodloader.api.mod.context.IModContext;

public interface IMod {
    default IModData getData() {
        return new ModAnnotationData(getClass());
    }

    IModContext buildContext();
}