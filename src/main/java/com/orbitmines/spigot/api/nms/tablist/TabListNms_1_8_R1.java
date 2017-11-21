package com.orbitmines.spigot.api.nms.tablist;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TabListNms_1_8_R1 implements TabListNms {

    public void send(Player player, String header, String footer) {
        IChatBaseComponent tab1 = ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tab2 = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter pack = new PacketPlayOutPlayerListHeaderFooter(tab1);

        try {
            Field field = pack.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(pack, tab2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
    }
}
