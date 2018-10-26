package com.openmodloader.core.util;

import com.openmodloader.api.NestedSupplier;
import com.openmodloader.loader.OpenModLoader;
import net.fabricmc.api.Side;

public class Sided<T> {
    private NestedSupplier<T> physicalClient;
    private NestedSupplier<T> physicalServer;

    private T value;

    public Sided<T> physicalClient(NestedSupplier<T> supplier) {
        this.physicalClient = supplier;
        return this;
    }

    public Sided<T> physicalServer(NestedSupplier<T> supplier) {
        this.physicalServer = supplier;
        return this;
    }

    public T get() {
        if (value == null) {
            value = this.compute();
        }
        return value;
    }

    private T compute() {
        Side side = OpenModLoader.getCurrentSide();
        NestedSupplier<T> supplier = side.isClient() ? this.physicalClient : this.physicalServer;
        if (supplier == null) {
            throw new IllegalStateException("Cannot compute sided value, supplier not present for side " + side);
        }
        return supplier.get();
    }
}
