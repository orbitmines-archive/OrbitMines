package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import com.orbitmines.spigot.api.utils.ItemUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveEnchantingTable implements Passive.Handler<PlayerDeathEvent> {

    private final Enchantment[] attack_enchantments = {Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT};

    private final Enchantment[] protect_enchantments = {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS};


    @Override
    public void trigger(PlayerDeathEvent event, int level) {

        Player entity = event.getEntity();
        Player kill = entity.getKiller();

        int slot = enchantedArmor(level) ? RandomUtils.i(0, 4) : 0;

        ItemStack item = getRandomBuilder(kill.getInventory(), slot);
        ItemMeta meta = item.getItemMeta();

        Enchantment[] enchantments = slot == 0 ? attack_enchantments : protect_enchantments;

        Enchantment enchantment = RandomUtils.randomFrom(enchantments);
        int l = -1;

        int tries = 0;

        while ((l == -1 || enchantment.getMaxLevel() <= l) && tries <= 10) {
            enchantment = RandomUtils.randomFrom(enchantments);
            l = meta.getEnchantLevel(enchantment) + 1;
            tries++;
        }

        item.addEnchantment(enchantment, l);

        this.setItem(kill, slot, item);

        /* Build Item Hologram */
        FloatingItem hologram = new FloatingItem(null, entity.getLocation()) {
            @Override
            public ItemStack build() {
                return new ItemStack(item);
            }
        };
        hologram.addLine(() -> Passive.ENCHANTING_TABLE.getColor().getChatColor() + "§l" + Passive.ENCHANTING_TABLE.getName(), false);
        Enchantment finalEnchantment = enchantment;
        int finalL = l;
        hologram.addLine(() -> "§e§o+ " + ItemUtils.getName(finalEnchantment, finalL), false);
        hologram.create(kill);

        new BukkitRunnable() {
            @Override
            public void run() {
                hologram.destroy();
            }
        }.runTaskLater(OrbitMines.getInstance(), 60L);
    }

    private ItemStack getRandomBuilder(PlayerInventory inventory, int slot) {
        switch (slot) {
            case 0:
                return inventory.getItemInMainHand();
            case 1:
                return inventory.getHelmet();
            case 2:
                return inventory.getChestplate();
            case 3:
                return inventory.getLeggings();
            case 4:
                return inventory.getBoots();
        }
        return null;
    }

    private void setItem(Player p, int slot, ItemStack item) {
        PlayerInventory inventory = p.getInventory();
        switch (slot) {
            case 0:
                inventory.setItemInMainHand(item);
                break;
            case 1:
                inventory.setHelmet(item);
                break;
            case 2:
                inventory.setChestplate(item);
                break;
            case 3:
                inventory.setLeggings(item);
                break;
            case 4:
                inventory.setBoots(item);
                break;
        }
    }

    private boolean enchantedArmor(int level) {
        switch (level) {
            case 2:
                return true;
            default:
                return false;
        }
    }
}
