package com.orbitmines.spigot.api.nms.tablist;

import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TabListNms_1_8_R1 implements TabListNms {

    public void send(Collection<? extends Player> players, String header, String footer) {
        IChatBaseComponent tab1 = ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tab2 = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter pack = new PacketPlayOutPlayerListHeaderFooter(tab1);

        try {
            ReflectionUtils.getDeclaredField(pack.getClass(), "b").set(pack, tab2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
        }
    }
}
