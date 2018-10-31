package com.openmodloader.core.event;

import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventDispatcher;

public class ChainedEventDispatcher implements IEventDispatcher {
    private final IEventDispatcher[] dispatchers;

    public ChainedEventDispatcher(IEventDispatcher[] dispatchers) {
        this.dispatchers = dispatchers;
    }

    @Override
    public <E extends IEvent> void dispatch(E event) {
        for (IEventDispatcher dispatcher : dispatchers) {
            dispatcher.dispatch(event);
        }
    }
}
