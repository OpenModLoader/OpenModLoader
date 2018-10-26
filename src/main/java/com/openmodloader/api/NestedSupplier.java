package com.openmodloader.api;

import java.util.function.Supplier;

@FunctionalInterface
public interface NestedSupplier<T> {
    Supplier<T> inner();

    default T get() {
        return this.inner().get();
    }
}
