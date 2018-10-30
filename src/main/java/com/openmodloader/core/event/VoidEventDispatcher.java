package com.openmodloader.core.event;

import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventDispatcher;

public final class VoidEventDispatcher implements IEventDispatcher {
    public static final VoidEventDispatcher INSTANCE = new VoidEventDispatcher();

    private VoidEventDispatcher() {
    }

    @Override
    public <E extends IEvent> void dispatch(E event) {
    }
}
