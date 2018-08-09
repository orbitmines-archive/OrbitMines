package com.orbitmines.spigot.api.nms.title;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TitleNms_1_8_R3 implements TitleNms {

    public void send(Collection<? extends Player> players, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent time = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + "" + "\"}");
        PacketPlayOutTitle timePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, time, fadeIn, stay, fadeOut);

        IChatBaseComponent t = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, t);

        IChatBaseComponent s = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, s);

        for (Player player : players) {
            PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;

            c.sendPacket(timePacket);
            c.sendPacket(titlePacket);
            c.sendPacket(subTitlePacket);
        }
    }
}
