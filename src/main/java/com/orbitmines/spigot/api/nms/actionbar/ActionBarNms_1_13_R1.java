package com.orbitmines.spigot.api.nms.actionbar;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import net.minecraft.server.v1_13_R1.ChatMessageType;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.PacketPlayOutChat;
import net.minecraft.server.v1_13_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ActionBarNms_1_13_R1 implements ActionBarNms {

    public void send(Collection<? extends Player> players, String actionBar) {
        IChatBaseComponent a = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + actionBar + "\"}");
        PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(a, ChatMessageType.GAME_INFO);

        for (Player player : players) {
            PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;
            c.sendPacket(actionBarPacket);
        }
    }
}
