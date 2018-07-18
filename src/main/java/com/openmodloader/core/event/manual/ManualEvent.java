package com.openmodloader.core.event.manual;

import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.event.EventContext;
import com.openmodloader.loader.ModInfo;
import com.openmodloader.loader.OpenModLoader;

public class ManualEvent<E extends Event> extends AbstractManualEvent<E,E> {
    public ManualEvent(E event, EventBus bus, EventContext context) {
        super(event, bus, context);
    }

    public E post(EventPhase phase) {
        if (event instanceof Event.WithResult)
            context.result = ((Event.WithResult) event).getDefaultResult();
        ModInfo previousMod = OpenModLoader.getActiveMod();
        context.phase = phase;
        bus.post(event, context);
        OpenModLoader.setCurrentPhase(null);
        OpenModLoader.setActiveMod(previousMod);
        return event;
    }
}