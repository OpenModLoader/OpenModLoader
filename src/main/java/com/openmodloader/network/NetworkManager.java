package com.openmodloader.network;

import com.openmodloader.loader.OpenModLoader;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.Side;
import net.fabricmc.api.Sided;
import net.minecraft.client.player.EntityPlayerClient;
import net.minecraft.entity.player.EntityPlayerServer;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.*;

public class NetworkManager {

    public static final Identifier CHANNEL = new Identifier("openmodloader", "custom");

    private static HashMap<Identifier, Class<? extends IPacket>> packetMap = new HashMap<>();

    //TODO are events used for registration?
    public static void registerPacket(Identifier identifier, Class<? extends IPacket> packet) {
	    if (packetMap.containsKey(identifier)) {
		    throw new RuntimeException("Packet already registered with this name!");
	    }
        if (packetMap.containsValue(packet)) {
            throw new RuntimeException("Packet already registered!");
        }
        packetMap.put(identifier, packet);
    }

    public static void handleIncomingPacket(PacketByteBuf byteBuf, Side side) {
        Identifier id = new Identifier(byteBuf.readString(byteBuf.readInt()));
        if (packetMap.containsKey(id)) {
            Class<? extends IPacket> packetClass = packetMap.get(id);
            IPacket packet;
            try {
                packet = packetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Failed to create new packet instance! does the packet class have a default constructor?", e);
            }
            packet.read(byteBuf);
            if (packet.handleOnMainThread()) {
                OpenModLoader.getSideHandler().runOnMainThread(() -> packet.handle(side));
            } else {
                packet.handle(side);
            }
        } else {
            //TODO kick the client for sending a back packet?
            System.out.println("Incoming packet was not registered with the server!");
        }
    }

    private static PacketByteBuf buildPacketData(IPacket packet) {
        if (!packetMap.containsValue(packet.getClass())) {
            throw new RuntimeException("Outgoing packet hasnt been registered!");
        }
        Identifier identifier = getKeysByValue(packetMap, packet.getClass()).get();
        PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
        byteBuf.writeInt(identifier.toString().length());
        byteBuf.a(identifier.toString());
        packet.write(byteBuf);
        return byteBuf;
    }

    @Sided(Side.CLIENT)
    public static void sendToServer(IPacket packet) {
        EntityPlayerClient clientPlayer = (EntityPlayerClient) OpenModLoader.getSideHandler().getClientPlayer();
        if (clientPlayer == null || clientPlayer.networkHandler == null) {
            throw new RuntimeException("Packet can only be sent to the server when the client is ingame");
        }
        clientPlayer.networkHandler.sendPacket(new SPacketCustomPayload(CHANNEL, buildPacketData(packet)));
    }

    public static void sendToPlayer(IPacket packet, EntityPlayerServer player) {
        player.networkHandler.a(new SPacketCustomPayload(CHANNEL, buildPacketData(packet)));
    }

    public static void sendToPlayers(IPacket packet, List<EntityPlayerServer> players) {
        SPacketCustomPayload packetPayload = new SPacketCustomPayload(CHANNEL, buildPacketData(packet));
        players.forEach(playerServer -> playerServer.networkHandler.a(packetPayload));

    }

    public static void sendToAll(IPacket packet) {
        MinecraftServer server = OpenModLoader.getSideHandler().getServer();
        //TODO find all the players and the packet
        throw new RuntimeException("Not working yet :)");
    }

    public static <T, E> Optional<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst();
    }

}
