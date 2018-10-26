package com.openmodloader.api.mod.config;

import com.openmodloader.api.NestedSupplier;
import com.openmodloader.loader.OpenModLoader;
import net.fabricmc.api.Side;

public class SidedModConfigurator implements IModConfigurator {
    private NestedSupplier<IModConfigurator> common;
    private NestedSupplier<IModConfigurator> physicalServer;
    private NestedSupplier<IModConfigurator> physicalClient;

    public SidedModConfigurator common(NestedSupplier<IModConfigurator> supplier) {
        this.common = supplier;
        return this;
    }

    public SidedModConfigurator physicalServer(NestedSupplier<IModConfigurator> supplier) {
        this.physicalServer = supplier;
        return this;
    }

    public SidedModConfigurator physicalClient(NestedSupplier<IModConfigurator> supplier) {
        this.physicalClient = supplier;
        return this;
    }

    @Override
    public void configure(IModConfig config) {
        if (this.common != null) {
            this.common.get().configure(config);
        }

        Side side = OpenModLoader.getCurrentSide();
        if (this.physicalClient != null && side.isClient()) {
            this.physicalClient.get().configure(config);
        }
        if (this.physicalServer != null && side.isServer()) {
            this.physicalServer.get().configure(config);
        }
    }
}
