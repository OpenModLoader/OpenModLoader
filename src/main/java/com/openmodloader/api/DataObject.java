package com.openmodloader.api;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface DataObject {

    boolean isPresent();

    void ifPresent(Consumer<DataObject> consumer);

    DataObject getChild(String name);

    DataArray getArray(String name);

    DataElement get(String name);

    <T> T get(String name, Class<T> type);

    <T> T orElse(String name, Class<T> type, T other);

    <T> T orElseGet(String name, Class<T> type, Supplier<T> supplier);

    interface DataArray {
        boolean isPresent();

        void ifPresent(Consumer<DataArray> consumer);

        DataElement get(int i) throws IndexOutOfBoundsException;

        Collection<DataElement> getAll();
    }

    interface DataElement {

        boolean isPresent();

        void ifPresent(Consumer<DataElement> consumer);

        <T> Optional<T> map(Function<DataElement, T> mapper);

        <T> T as(Class<T> type) throws NullPointerException;

        <T> T orElse(Class<T> type, T other);

        <T> T orElseGet(Class<T> type, Supplier<T> supplier);
    }
}