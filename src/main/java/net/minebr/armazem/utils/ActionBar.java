package net.minebr.armazem.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

    public static void sendActionText(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat((IChatBaseComponent)new ChatComponentText(message), (byte)2);
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)packet);
    }

}
