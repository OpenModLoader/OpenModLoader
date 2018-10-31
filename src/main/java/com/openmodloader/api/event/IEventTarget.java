package com.openmodloader.api.event;

public interface IEventTarget<E extends IEvent> {
    Class<E> getType();

    boolean canReceive(IEventTarget<?> target);
}
