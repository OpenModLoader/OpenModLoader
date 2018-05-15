package com.openmodloader.core.event;

import com.openmodloader.api.event.EventPhase;

public class EventContext {
    public boolean cancelled;
    public EventPhase phase;
    public Object result;
}
