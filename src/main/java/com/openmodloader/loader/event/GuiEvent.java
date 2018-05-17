package com.openmodloader.loader.event;

import com.openmodloader.api.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.FontRenderer;

import java.lang.reflect.Type;

public class GuiEvent<G extends GuiScreen> implements Event.Generic {
    private G gui;
    private Class<G> type;

    public GuiEvent(G gui) {
        this.gui = gui;
        this.type = (Class<G>) gui.getClass();
    }

    public G getGui() {
        return gui;
    }

    public Minecraft getClient() {
        return Minecraft.getInstance();
    }

    public FontRenderer getFontRenderer() {
        return getClient().fontRenderer;
    }

    @Override
    public boolean matchesGenericType(Class<? extends Generic> eventType, int index, Type type) {
        return this.type == type;
    }

    public static class Draw<G extends GuiScreen> extends GuiEvent<G> {
        public Draw(G gui) {
            super(gui);
        }
    }

    public static class Open<G extends GuiScreen> extends GuiEvent<G> {
        public Open(G gui) {
            super(gui);
        }
    }
}