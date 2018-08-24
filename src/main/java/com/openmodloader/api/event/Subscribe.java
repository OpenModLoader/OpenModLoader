package com.openmodloader.api.event;

import com.openmodloader.core.event.EventBus;
import com.openmodloader.loader.OpenModLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    EventPhase phase() default EventPhase.DEFAULT;
}
