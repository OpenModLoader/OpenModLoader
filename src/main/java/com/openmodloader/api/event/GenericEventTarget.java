package com.openmodloader.api.event;

public class GenericEventTarget<E extends IEvent, G> implements IEventTarget<E> {
    private final Class<E> eventType;
    private final Class<G> genericType;

    protected GenericEventTarget(Class<E> eventType, Class<G> genericType) {
        this.eventType = eventType;
        this.genericType = genericType;
    }

    @SuppressWarnings("unchecked")
    public static <E extends IEvent, G> GenericEventTarget<E, G> of(Class<?> eventClass, Class<?> genericType) {
        return new GenericEventTarget<>((Class<E>) eventClass, (Class<G>) genericType);
    }

    @Override
    public Class<E> getType() {
        return this.eventType;
    }

    @Override
    public boolean canReceive(IEventTarget<?> target) {
        if (target.getType().equals(this.eventType)) {
            if (target instanceof GenericEventTarget) {
                GenericEventTarget<?, ?> genericTarget = (GenericEventTarget<?, ?>) target;
                return this.genericType.isAssignableFrom(genericTarget.genericType);
            }
            return true;
        }
        return false;
    }
}
