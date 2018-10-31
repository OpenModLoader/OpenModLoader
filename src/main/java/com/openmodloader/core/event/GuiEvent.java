package com.openmodloader.core.event;

import com.openmodloader.api.event.GenericEventTarget;
import com.openmodloader.api.event.IEvent;
import com.openmodloader.api.event.IEventTarget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.text.TextRenderer;

public abstract class GuiEvent<T extends GuiScreen> implements IEvent {
    protected final T gui;

    public GuiEvent(T gui) {
        this.gui = gui;
    }

    public T getGui() {
        return gui;
    }

    public static class Open<T extends GuiScreen> extends GuiEvent<T> {
        public Open(T gui) {
            super(gui);
        }

        @Override
        public IEventTarget<?> makeTarget() {
            return target(gui.getClass());
        }

        public static <T extends GuiScreen> IEventTarget<Open<T>> target(Class<T> target) {
            return GenericEventTarget.of(Open.class, target);
        }

        public static <T extends GuiScreen> IEventTarget<Open<T>> target() {
            return GenericEventTarget.of(Open.class, GuiScreen.class);
        }
    }

    public static class Draw<T extends GuiScreen> extends GuiEvent<T> {
        protected final TextRenderer textRenderer;

        public Draw(T gui, TextRenderer textRenderer) {
            super(gui);
            this.textRenderer = textRenderer;
        }

        public TextRenderer getTextRenderer() {
            return textRenderer;
        }

        @Override
        public IEventTarget<?> makeTarget() {
            return target(gui.getClass());
        }

        public static <T extends GuiScreen> IEventTarget<Draw<T>> target(Class<T> target) {
            return GenericEventTarget.of(Draw.class, target);
        }

        public static <T extends GuiScreen> IEventTarget<Draw<T>> target() {
            return GenericEventTarget.of(Draw.class, GuiScreen.class);
        }
    }
}
