package com.orbitmines.spigot.api.nms.title;

import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TitleNms_1_8_R1 implements TitleNms {

    public void send(Collection<? extends Player> players, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent time = ChatSerializer.a("{\"text\": \"" + "" + "\"}");
        PacketPlayOutTitle timePacket = new PacketPlayOutTitle(EnumTitleAction.TIMES, time, fadeIn, stay, fadeOut);

        IChatBaseComponent t = ChatSerializer.a("{\"text\": \"" + title + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, t);

        IChatBaseComponent s = ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, s);

        for (Player player : players) {
            PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;

            c.sendPacket(timePacket);
            c.sendPacket(titlePacket);
            c.sendPacket(subTitlePacket);
        }
    }
}
