package com.orbitmines.spigot.api.nms.particles;

import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Fadi on 11-5-2016.
 */
public class ParticleNms_1_8_R2 implements ParticleNms {

    @Override
    public void send(Collection<? extends Player> players, Particle particle, boolean longDistance, float x, float y, float z, float xOff, float yOff, float zOff, float speed, int amount, int... args) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle.toString()), longDistance, x, y, z, xOff, yOff, zOff, speed, amount, args);

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
