package com.openmodloader.loader.event;

import com.openmodloader.api.event.Event;

public class LoadEvent implements Event {
    private final Stage stage;

    public LoadEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public enum Stage {
        CONSTRUCTION,
        FINALIZATION
    }

    public static class Construction extends LoadEvent {
        public Construction() {
            super(Stage.CONSTRUCTION);
        }
    }

    public static class Finalization extends LoadEvent {
        public Finalization() {
            super(Stage.FINALIZATION);
        }
    }
}