package com.orbitmines.spigot.servers.hub.handlers.Cosmetics;


import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class ComposedCosmetic extends Cosmetic {

    private ComposedItem item;

    private ComposedCosmetic(ComposedItem item) {
        this.item = item;
    }

    public ComposedCosmetic setPlayer(OMPlayer player) {
        item.setPlayer(player);
        return this;
    }

    @Override
    public void update() {
        item.update();
    }

    public static ComposedCosmetic ChristmasHat (OMPlayer p) {
        ComposedItemPart.PartType small = ComposedItemPart.PartType.SMALL_BLOCK;
        Material red = Material.RED_WOOL;
        ComposedItem hat = new ComposedItem(p, ComposedItem.TargetType.HEAD, new Vector(0,-1.15,0))
                .addPart(ComposedItemPart.buildPart(ComposedItemPart.PartType.MEDIUM_BLOCK, Material.SNOW, new Location(p.getWorld(), 0,0,0)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.1,0.08,-0.14)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.07,0.08,-0.1)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.14,0.08,-0.04)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.16,0.08,0.14)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.01,0.08,0.16)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.01,0.08,0.11)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.09,0.08,0.08)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.16,0.08,0)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.14,0.2,-0.04)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.1,0.2,-0.09)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.02,0.2,-0.06)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.08,0.2,0)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.08,0.2,0.07)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.05,0.2,0.05)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.13,0.32,-0.07)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.1,0.32,-0.11)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.01,0.32,-0.13)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),0.03,0.32,0.01)))
                .addPart(ComposedItemPart.buildPart(small, red, new Location(p.getWorld(),-0.02,0.32,-0.01)))
                .addPart(ComposedItemPart.buildPart(small, Material.WHITE_WOOL, new Location(p.getWorld(),0.14,0.46,-0.15)));
        return new ComposedCosmetic(hat);
    }
}
