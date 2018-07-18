package com.openmodloader.core.event.manual;

import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.event.EventContext;

public abstract class AbstractManualEvent<E extends Event,RETURN> {
    protected final E event;
    protected final EventBus bus;
    protected final EventContext context;

    public AbstractManualEvent(E event, EventBus bus, EventContext context) {
        this.event = event;
        this.bus = bus;
        this.context = context;
    }

    public abstract RETURN post(EventPhase phase);
}