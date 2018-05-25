package com.orbitmines.spigot.api.nms.particles;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 11-5-2016.
 */
public class ParticleNms_1_12_R1 implements ParticleNms {

    public void send(Collection<? extends Player> players, String particle, boolean longDistance, float x, float y, float z, float xOff, float yOff, float zOff, float speed, int amount, int... args) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle), longDistance, x, y, z, xOff, yOff, zOff, speed, amount, args);

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
