package com.openmodloader.loader.client;

import com.openmodloader.api.loader.SideHandler;
import net.fabricmc.api.Side;

public class ClientSideHandler implements SideHandler {
    @Override
    public Side getSide() {
        return Side.CLIENT;
    }
}
