package com.openmodloader.api.event;

public class GenericEventTarget<E extends IEvent, G> implements IEventTarget<E> {
    private final Class<E> eventClass;
    private final Class<G> genericType;
    private final int hash;

    GenericEventTarget(Class<E> eventClass, Class<G> genericType) {
        this.eventClass = eventClass;
        this.genericType = genericType;
        this.hash = this.computeHash();
    }

    @SuppressWarnings("unchecked")
    public static <E extends IEvent, G> GenericEventTarget<E, G> of(Class<?> eventClass, Class<?> genericType) {
        return new GenericEventTarget<>((Class<E>) eventClass, (Class<G>) genericType);
    }

    private int computeHash() {
        return this.eventClass.hashCode() + this.genericType.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (obj.getClass() == this.getClass() || obj instanceof GenericEventTarget) {
            GenericEventTarget<?, ?> target = (GenericEventTarget<?, ?>) obj;
            return target.eventClass.equals(this.eventClass) && target.genericType.equals(this.genericType);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
