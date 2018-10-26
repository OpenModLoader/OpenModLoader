package com.openmodloader;

import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.api.mod.config.SidedModConfigurator;
import com.openmodloader.api.mod.config.SimpleEventConfig;
import com.openmodloader.core.event.GuiEvent;
import net.minecraft.client.gui.GuiScreen;

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
                    .listen(GuiEvent.Open.target(GuiScreen.class), (event, context) -> {
                        GuiScreen gui = event.getGui();
                        System.out.println("opened screen " + gui);
                    })
                    .build();
        }
    }
}
