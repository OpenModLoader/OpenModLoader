package com.openmodloader;

import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.api.mod.config.SidedModConfigurator;
import com.openmodloader.api.mod.config.SimpleEventConfig;
import com.openmodloader.core.event.PretendGuiEvent;
import net.minecraft.client.gui.menu.GuiMainMenu;

public class TestMod implements IModConfigurator {
    @Override
    public void configure(IModConfig config) {
        config.apply(new SidedModConfigurator()
                .physicalClient(() -> ClientConfigurator::new)
        );
    }

    private static class ClientConfigurator implements IModConfigurator {
        @Override
        public void configure(IModConfig config) {
            config.addEventConfig(buildEventConfig());
        }

        private SimpleEventConfig buildEventConfig() {
            return SimpleEventConfig.builder()
                    .listen(PretendGuiEvent.target(GuiMainMenu.class), (event, context) -> {
                        GuiMainMenu gui = event.getGui();
                        System.out.println("event received for " + gui);
                    })
                    .build();
        }
    }
}
