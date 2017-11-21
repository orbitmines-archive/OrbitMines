package com.orbitmines.spigot.api.nms.title;

import com.orbitmines.spigot.api.handlers.chat.Title;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_11_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TitleNms_1_11_R1 implements TitleNms {

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
