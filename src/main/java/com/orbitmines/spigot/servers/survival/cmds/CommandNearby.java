package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.utils.VectorUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.region.Region;
import org.bukkit.block.BlockFace;

public class CommandNearby extends Command {

    private String[] alias = { "/near", "/nearby", "/nearbyregion", "/nearbyrg" };

    private final Survival survival;

    public CommandNearby(Survival survival) {
        super(Server.SURVIVAL);

        this.survival = survival;
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (!omp.getWorld().getName().equals(survival.getWorld().getName())) {
            omp.setLastRegion(null);
            omp.sendMessage("Region", Color.RED, "Er zijn geen regions dichtbij.", "There are no regions nearby.");
            return;
        }

        Region region = Region.getNearest(omp.getLocation());
        omp.setLastRegion(region);

        int blocks = (int) VectorUtils.distance2D(omp.getLocation().toVector(), region.getLocation().toVector());
        int regionId = region.getId() + 1;

        if (blocks <= Region.PROTECTION) {
            omp.sendMessage("Region", Color.LIME, "Je bent op §aRegion " + regionId + "§7.", "You are currently at §aRegion " + regionId + "§7.");
            return;
        }

        BlockFace face = WorldUtils.fromYaw(WorldUtils.getYaw(omp.getLocation(), region.getLocation()));

        StringBuilder stringBuilder = new StringBuilder();
        String[] parts = face.toString().split("_");

        for (int i = 0; i < parts.length; i++) {
            if (i != 0)
                stringBuilder.append(" ");

            stringBuilder.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1).toLowerCase());
        }

        omp.sendMessage("Region", Color.LIME, "§aRegion " + regionId + "§7 is de meest dichtsbijzijnde region. (§a" + NumberUtils.locale(blocks) + " Blocks§7, §a" + stringBuilder.toString() + "§7)", "§aRegion " + regionId + "§7 is the nearest region. (§a" + NumberUtils.locale(blocks) + " Blocks§7, §a" + stringBuilder.toString() + "§7)");
    }
}
