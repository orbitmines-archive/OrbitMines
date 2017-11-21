package com.orbitmines.spigot.api.handlers.npc;

import com.madblock.spigot.MadBlock;
import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class CustomItem {


    private static List<CustomItem> customItems = new ArrayList<>();

    private MadBlock madBlock;

    private Location location;

    private List<ItemInstance> itemInstances;

    private CustomItem.PickUpAction pickUpAction;

    public CustomItem(Location location) {
        this(location, null);
    }

    public CustomItem(Location location, CustomItem.PickUpAction pickUpAction) {
        madBlock = MadBlock.getInstance();

        customItems.add(this);

        this.location = location;
        this.itemInstances = new ArrayList<>();
        this.pickUpAction = pickUpAction;
    }

    public Location getLocation() {
        return location;
    }

    public boolean canPickUp() {
        return pickUpAction != null;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void spawn() {
        clear();

        for (CustomItem.ItemInstance itemInstance : itemInstances) {
            itemInstance.spawn();
        }

        CustomItem customItem = this;

        new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run() {
                for (CustomItem.ItemInstance itemInstance : itemInstances) {
                    Item item = itemInstance.getItem();

                    if (item == null || item.isDead()) {
                        cancel();

                        if (customItems.contains(customItem))
                            spawn();
                    } else {
                        item.setTicksLived(1);
                    }
                }
            }
        };
    }

    public void clear() {
        for (CustomItem.ItemInstance itemInstance : itemInstances) {
            itemInstance.clear();
        }
    }

    public void delete() {
        clear();
        customItems.remove(this);
    }

    public void pickUp(PlayerPickupItemEvent event, OMPlayer player) {
        pickUpAction.pickUp(event, player, this);

        if (!event.isCancelled()) {
            event.setCancelled(true);
            delete();
        }
    }

    public static CustomItem getCustomItem(Item item) {
        for (CustomItem customItem : customItems) {
            for (CustomItem.ItemInstance itemInstance : customItem.itemInstances) {
                if (itemInstance.item != null && itemInstance.item.getEntityId() == item.getEntityId())
                    return customItem;
            }
        }

        return null;
    }

    public static List<CustomItem> getCustomItems() {
        return customItems;
    }

    public static abstract class PickUpAction {

        public abstract void pickUp(PlayerPickupItemEvent event, OMPlayer player, CustomItem item);

    }

    public static abstract class ItemInstance {

        private CustomItem customItem;
        private Item item;
        private boolean hideOnJoin;
        private Set<Player> watchers;

        public ItemInstance(CustomItem customItem, boolean hideOnJoin) {
            this.customItem = customItem;
            this.hideOnJoin = hideOnJoin;
            this.watchers = new HashSet<>();
            this.customItem.itemInstances.add(this);
        }

        public abstract ItemStack getItemStack();

        public abstract String getDisplayName();

        public abstract boolean isDisplayNameVisible();

        public CustomItem getCustomItem() {
            return customItem;
        }

        public Item getItem() {
            return item;
        }

        public boolean hideOnJoin() {
            return hideOnJoin;
        }

        public void spawn() {
            spawn((Collection<? extends Player>) null);
        }

        public void spawn(Player... createFor) {
            spawn(Arrays.asList(createFor));
        }

        public void spawn(Collection<? extends Player> createFor) {
            if (watchers.size() != 0)
                createFor = watchers;

            item = customItem.location.getWorld().dropItem(customItem.location, getItemStack());

            if (!customItem.canPickUp())
                item.setPickupDelay(Integer.MAX_VALUE);

            item.setCustomName(getDisplayName());
            item.setCustomNameVisible(isDisplayNameVisible());

            if (createFor == null)
                return;

            watchers.addAll(createFor);

            createForWatchers();
        }

        public void clear() {
            if (item != null)
                item.remove();
        }

        public void delete() {
            clear();

            customItem.getItemInstances().remove(this);
        }

        public void createForWatchers() {
            List<Player> hideFor = new ArrayList<>();
            for (Player player : customItem.location.getWorld().getPlayers()) {
                if (!watchers.contains(player))
                    hideFor.add(player);
            }

            hideFor(hideFor);
        }

        public Set<Player> getWatchers() {
            return watchers;
        }

        public void hideFor(Player player) {
            hideFor(Collections.singletonList(player));
        }

        public void hideFor(Collection<? extends Player> players) {
            MadBlock.getInstance().getNms().entity().destroyEntityFor(players, item);
        }

        public void updateItemStack() {
            item.setItemStack(getItemStack());
        }

        public void updateName() {
            item.setCustomName(getDisplayName());
            item.setCustomNameVisible(isDisplayNameVisible());

            createForWatchers();
        }
    }
}
