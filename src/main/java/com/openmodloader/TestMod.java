package com.openmodloader;

import com.openmodloader.api.mod.IMod;
import com.openmodloader.api.mod.Mod;
import com.openmodloader.api.mod.context.IModContext;

@Mod(id = "test", version = "1.0.0")
public class TestMod implements IMod {
    @Override
    public IModContext buildContext() {
        return null;
    }
}