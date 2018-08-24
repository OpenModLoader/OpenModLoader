package com.openmodloader.api.event;

import java.lang.reflect.Type;

public interface Event {

    interface Cancellable extends Event {

    }

    interface PhaseLimit extends Event {
        EventPhase[] getPossiblePhases();
    }

    interface Generic extends Event {
        boolean matchesGenericType(Class<? extends Event.Generic> eventType, int index, Type type);
    }

    interface WithResult<T> extends Event {
        T getDefaultResult();
    }
}