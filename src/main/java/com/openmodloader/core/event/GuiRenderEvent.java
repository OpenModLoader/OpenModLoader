package com.openmodloader.core.event;

import com.openmodloader.api.event.GenericEventTarget;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventTarget;
import net.minecraft.client.gui.Gui;

public class GuiRenderEvent<T extends Gui> implements IEvent {
    private final T gui;

    public GuiRenderEvent(T gui) {
        this.gui = gui;
    }

    public T getGui() {
        return gui;
    }

    @Override
    public IEventTarget<?> makeTarget() {
        return target(gui.getClass());
    }

    public static <T extends Gui> IEventTarget<GuiRenderEvent<T>> target(Class<T> target) {
        return GenericEventTarget.of(GuiRenderEvent.class, target);
    }
}
