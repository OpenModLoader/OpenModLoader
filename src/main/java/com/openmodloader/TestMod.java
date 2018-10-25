package com.openmodloader;

import com.openmodloader.api.IMod;
import com.openmodloader.api.IModData;

public class TestMod implements IMod {
    @Override
    public IModData getData() {
        return new IModData() {
            @Override
            public String getModId() {
                return "test";
            }

            @Override
            public String getModVersion() {
                return "1.0.0";
            }
        };
    }
}
