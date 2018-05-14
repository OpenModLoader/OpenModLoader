package com.openmodloader.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

public interface Event {

    interface Cancellable extends Event {

    }
    interface Generic extends Event {
        boolean matchesGenericType(Class<? extends Event.Generic> eventType, int index, Type type);
    }

    interface WithResult<T> extends Event {
        T getDefaultResult();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Subscribe {
        EventPhase phase() default EventPhase.DEFAULT;
    }
}