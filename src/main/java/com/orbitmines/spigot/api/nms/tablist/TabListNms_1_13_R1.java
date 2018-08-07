package com.orbitmines.spigot.api.nms.tablist;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;

public class TabListNms_1_13_R1 implements TabListNms {

    public void send(Collection<? extends Player> players, String header, String footer) {
        IChatBaseComponent tab1 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tab2 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter pack = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = pack.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(pack, tab1);

            Field footerField = pack.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(pack, tab2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
        }
    }
}
