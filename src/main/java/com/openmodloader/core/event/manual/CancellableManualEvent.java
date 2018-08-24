package com.openmodloader.core.event.manual;

import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.core.event.EventBus;
import com.openmodloader.core.event.EventContext;
import com.openmodloader.loader.OpenModLoader;

public class CancellableManualEvent<E extends Event.Cancellable> extends AbstractManualEvent<E, Boolean> {
    public CancellableManualEvent(E event, EventBus bus, EventContext context) {
        super(event, bus, context);
    }

    @Override
    public Boolean post(EventPhase phase) {
        EventContext context = new EventContext();
        if (event instanceof Event.WithResult)
            context.result = ((Event.WithResult) event).getDefaultResult();
        ModInfo previousMod = OpenModLoader.getActiveMod();
        context.phase = phase;
        bus.post(event, context);
        OpenModLoader.setCurrentPhase(null);
        OpenModLoader.setActiveMod(previousMod);
        return context.cancelled;
    }
}