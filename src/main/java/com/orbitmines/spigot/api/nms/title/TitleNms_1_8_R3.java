package com.orbitmines.spigot.api.nms.title;

import com.orbitmines.spigot.api.handlers.chat.Title;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TitleNms_1_8_R3 implements TitleNms {

    public void send(Player player, Title title) {
        IChatBaseComponent time = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + "" + "\"}");
        PacketPlayOutTitle timePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, time, title.getFadeIn(), title.getStay(), title.getFadeOut());

        IChatBaseComponent t = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title.getTitle() + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, t);

        IChatBaseComponent s = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title.getSubTitle() + "\"}");
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, s);

        PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;

        c.sendPacket(timePacket);
        c.sendPacket(titlePacket);
        c.sendPacket(subTitlePacket);
    }
}
