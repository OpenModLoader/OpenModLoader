package com.openmodloader;

import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.api.mod.config.SidedModConfigurator;
import com.openmodloader.api.mod.config.SimpleEventConfig;
import com.openmodloader.api.mod.config.SimpleRegistrationConfig;
import com.openmodloader.core.event.GuiEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemInteraction;

public class TestMod implements IModConfigurator {
    private static final String ID = "test";

    @Override
    public void configure(IModConfig config) {
        config.addRegistrationConfig(SimpleRegistrationConfig.builder()
                .withEntry(Registry.ITEMS, new Identifier(ID, "foo"), identifier -> new TestItem(new Item.Builder()
                        .withItemGroup(ItemGroup.MISC)
                ))
                .build()
        );

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

    private static class TestItem extends Item {
        TestItem(Builder builder) {
            super(builder);
        }

        @Override
        public ActionResult onItemUse(ItemInteraction interaction) {
            EntityPlayer player = interaction.getPlayer();
            player.s += 1.0;

            return ActionResult.SUCCESS;
        }
    }
}
