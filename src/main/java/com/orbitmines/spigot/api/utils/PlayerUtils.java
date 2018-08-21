package com.orbitmines.spigot.api.utils;

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayerUtils {

    public static void updateInventory(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(OrbitMines.getInstance(), 1);
    }

    public static Block getTargetBlock(LivingEntity livingEntity, int range) {
        BlockIterator blockIterator = new BlockIterator(livingEntity, range);
        Block lastBlock = blockIterator.next();
        while (blockIterator.hasNext()) {
            lastBlock = blockIterator.next();
            if (lastBlock.getType() == Material.AIR)
                continue;

            break;
        }
        return lastBlock;
    }

    public static boolean isInside(Player player, Block block) {
        Location hitBox1_A = player.getLocation().subtract(0.3, 0, 0.3);
        Location hitBox1_B = player.getLocation().add(0.3, 1.8, 0.3);

        Location hitBox2_A = block.getLocation();
        Location hitBox2_B = block.getLocation().add(1, 1, 1);

        return hitBox1_A.getX() < hitBox2_B.getX() && hitBox1_B.getX() > hitBox2_A.getX() && hitBox1_A.getY() < hitBox2_B.getY() && hitBox1_B.getY() > hitBox2_A.getY();
    }

    /* ItemStack */
    public static int getEmptySlotCount(Inventory inventory) {
        int count = 0;

        for (ItemStack item : inventory.getStorageContents()) {
            if (item == null || item.getType() == Material.AIR)
                count++;
        }

        return count;
    }

    public static int getSlotsRequired(Material material, int amount) {
        int maxStackSize = material.getMaxStackSize();
        int leftOver = maxStackSize % amount;

        return leftOver == 0 ? amount / maxStackSize : (amount + maxStackSize - leftOver) /  maxStackSize;
    }

    public static boolean hasItems(Player player, Material material, int amount) {
        return getAmount(player, material) >= amount;
    }

    public static List<ItemStack> removeItems(Player player, Material material, int amount) {
        return removeItems(player.getInventory(), material, amount);
    }

    public static List<ItemStack> removeItems(Inventory inventory, Material material, int amount) {
        List<ItemStack> removed = new ArrayList<>();

        int count = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() != material)
                continue;

            if (count + item.getAmount() <= amount) {
                count += item.getAmount();
                inventory.setItem(inventory.first(item), null);
                removed.add(item);

                if (count == amount)
                    return removed;

            } else {
                /* LeftOvers */
                ItemStack newItem = new ItemStack(item);
                newItem.setAmount(item.getAmount() - (amount - count));

                ItemStack removedItem = new ItemStack(item);
                removedItem.setAmount(amount - count);

                inventory.setItem(inventory.first(item), newItem);
                removed.add(removedItem);

                break;
            }
        }

        return removed;
    }

    public static int getAmount(Player player, Material material) {
        return getAmount(player, material, -1);
    }

    public static int getAmount(Player player, Material material, int durability) {
        return getAmount(player.getInventory(), material, durability);
    }

    public static int getAmount(Inventory inventory, Material material) {
        return getAmount(inventory, material, -1);
    }

    public static int getAmount(Inventory inventory, Material material, int durability) {
        int amount = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material && (durability == -1 || item.getDurability() == durability))
                amount += item.getAmount();
        }

        return amount;
    }

    public static void removeOneHeldItem(Player player) {
        ItemStack item = player.getItemOnCursor();

        if (item.getAmount() != 1) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), item);
        } else {
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
        }
    }
}
