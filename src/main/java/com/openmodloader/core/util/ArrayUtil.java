package com.openmodloader.core.util;

import java.util.function.Consumer;

public class ArrayUtil {

    public static <T> void forEach(T[] array, Consumer<T> consumer) {
        for (T t : array)
            consumer.accept(t);
    }
}
