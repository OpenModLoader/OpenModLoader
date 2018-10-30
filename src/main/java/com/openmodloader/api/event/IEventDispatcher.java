package com.openmodloader.api.event;

public interface IEventDispatcher {
    <E extends IEvent> void dispatch(E event);
}
