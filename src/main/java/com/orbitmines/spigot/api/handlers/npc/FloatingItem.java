package com.orbitmines.spigot.api.handlers.npc;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FloatingItem extends Hologram {

    private static List<FloatingItem> floatingItems = new ArrayList<>();

    private Item item;
    private ArmorStand clickBox;

    private ItemBuilder itemBuilder;

    public FloatingItem(ItemBuilder itemBuilder, Location spawnLocation) {
        super(spawnLocation);

        this.itemBuilder = itemBuilder;
        addLine(() -> null, true);
        addLine(() -> null, true);
    }

    public FloatingItem(ItemBuilder itemBuilder, Location spawnLocation, double yOff) {
        super(spawnLocation, yOff, Face.DOWN);

        this.itemBuilder = itemBuilder;
        addLine(() -> null, true);
        addLine(() -> null, true);
    }

    @Override
    protected void spawn() {
        super.spawn();

        clickBox = OrbitMines.getInstance().getNms().armorStand().spawn(spawnLocation.clone().subtract(0, getYOff() - Hologram.Y_OFFSET_PER_LINE * 3, 0), false);
        clickBox.setGravity(false);

        item = spawnLocation.getWorld().dropItem(spawnLocation, itemBuilder.build());
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setInvulnerable(true);

        lines.get(0).getArmorStand().addPassenger(item);
    }

    @Override
    protected void despawn() {
        super.despawn();

        if (clickBox != null)
            clickBox.remove();
        if (item != null)
            item.remove();
    }

    @Override
    public void update() {
        super.update();

        item.setItemStack(itemBuilder.build());
    }

    @Override
    protected Collection<Entity> getEntities() {
        Collection<Entity> entities = super.getEntities();
        entities.add(item);
        entities.add(clickBox);

        return entities;
    }

    @Override
    protected void addToList() {
        super.addToList();

        floatingItems.add(this);
    }

    @Override
    protected void removeFromList() {
        super.removeFromList();

        floatingItems.remove(this);
    }

    @Override
    public void setSpawnLocation(Location spawnLocation) {
        super.setSpawnLocation(spawnLocation);
    }

//    public ArmorStand getVehicle() {
//        return vehicle;
//    }

    public Item getItem() {
        return item;
    }

    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public void setItemBuilder(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    public static List<FloatingItem> getFloatingItems() {
        return floatingItems;
    }

    public static FloatingItem getFloatingItem(Entity entity) {
        for (FloatingItem npc : floatingItems) {
            for (Entity en : npc.getEntities()) {
                if (en.getEntityId() == entity.getEntityId())
                    return npc;
            }
        }
        return null;
    }
}
