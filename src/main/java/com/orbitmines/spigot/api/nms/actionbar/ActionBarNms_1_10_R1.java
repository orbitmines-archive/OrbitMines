package com.orbitmines.spigot.api.nms.actionbar;

import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ActionBarNms_1_10_R1 implements ActionBarNms {

    public void send(Player player, ActionBar actionBar) {
        IChatBaseComponent a = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + actionBar.getMessage() + "\"}");
        PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(a, (byte) 2);

        PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;
        c.sendPacket(actionBarPacket);
    }
}
