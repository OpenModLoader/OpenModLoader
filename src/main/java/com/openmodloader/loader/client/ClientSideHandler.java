package com.openmodloader.loader.client;

import com.openmodloader.api.loader.Side;
import com.openmodloader.api.loader.SideHandler;

public class ClientSideHandler implements SideHandler {
    @Override
    public Side getSide() {
        return Side.CLIENT;

    }
}
