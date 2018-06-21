package com.openmodloader.api.data;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DataObject<V> {
    private static DataObject<?> EMPTY = new DataObject<>(null);
    private V value;

    private DataObject(V value) {
        this.value = value;
    }

    public static <V> DataObject<V> of(V value) {
        return value == null ? empty() : new DataObject<>(value);
    }

    public static <V> DataObject<V> empty() {
        return (DataObject<V>) EMPTY;
    }

    @Nullable
    public V get() {
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public DataObject<V> ifPresent(Consumer<V> consumer) {
        if (isPresent())
            consumer.accept(value);
        return this;
    }

    public DataObject<V> ifPresent(Runnable runnable) {
        if (isPresent())
            runnable.run();
        return this;
    }

    public V orElse(V other) {
        return isPresent() ? value : other;
    }

    public V orElse(Supplier<V> supplier) {
        return isPresent() ? value : supplier.get();
    }

    public Stream<V> stream() {
        return isPresent() ? Stream.of(value) : Stream.empty();
    }

    public <T> DataObject<T> map(Function<V, T> mapper) {
        return isPresent() ? DataObject.of(mapper.apply(value)) : DataObject.empty();
    }

    public <T> DataObject<T> flatMap(Function<V, DataObject<T>> mapper) {
        return isPresent() ? Preconditions.checkNotNull(mapper.apply(value)) : empty();
    }

    public <X extends Throwable> V orElseThrows(Supplier<X> supplier) throws X {
        if (isPresent())
            return value;
        throw supplier.get();
    }
}
