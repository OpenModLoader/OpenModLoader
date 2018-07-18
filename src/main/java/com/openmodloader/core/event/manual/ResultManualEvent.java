package com.openmodloader.core.event.manual;

import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.event.EventContext;
import com.openmodloader.loader.ModInfo;
import com.openmodloader.loader.OpenModLoader;

public class ResultManualEvent<T,E extends Event.WithResult<T>> extends AbstractManualEvent<E,T> {
    public ResultManualEvent(E event, EventBus bus, EventContext context) {
        super(event, bus, context);
    }

    public T post(EventPhase phase) {
        EventContext context = new EventContext();
        context.result = event.getDefaultResult();
        ModInfo previousMod = OpenModLoader.getActiveMod();
        context.phase = phase;
        bus.post(event, context);
        OpenModLoader.setCurrentPhase(null);
        OpenModLoader.setActiveMod(previousMod);
        return (T) context.result;
    }
}