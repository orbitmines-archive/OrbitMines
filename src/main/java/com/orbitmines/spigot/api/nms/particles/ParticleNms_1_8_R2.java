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
    public void send(Collection<? extends Player> players, Particle particle, float x, float y, float z, float xSize, float ySize, float zSize, float special, int amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle.toString()), true, x, y, z, xSize, ySize, zSize, special, amount);

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
