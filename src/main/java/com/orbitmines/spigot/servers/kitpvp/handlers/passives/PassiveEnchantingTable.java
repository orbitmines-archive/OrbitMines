package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PassiveEnchantingTable implements Passive.Handler<PlayerDeathEvent> {

    private final Enchantment[] attack_enchantments = {Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT};

    private final Enchantment[] protect_enchantments = {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS};


    @Override
    public void trigger(PlayerDeathEvent event, int level) {

        Player entity = event.getEntity();
        Player kill = entity.getKiller();

        KitPvPPlayer killer = KitPvPPlayer.getPlayer(kill);
        Kit kit = killer.getSelectedKit().getKit();

        int slot = enchantedArmor(level) ? RandomUtils.i(0, 4) : 0;

        System.out.println(slot);

        ItemBuilder item = getRandomBuilder(kit, slot);

        Enchantment[] enchantments = slot == 0 ? attack_enchantments : protect_enchantments;

        Enchantment enchantment = RandomUtils.randomFrom(enchantments);
        int l = -1;

        int tries = 0;

        while ((l == -1 || enchantment.getMaxLevel() <= l) && tries <= 10) {
            enchantment = RandomUtils.randomFrom(enchantment);
            l = item.getEnchantments().getOrDefault(enchantment, 0) + 1;
            tries++;
            System.out.println(enchantment.getKey().getKey() + ":" + l);
        }

        item.getEnchantments().put(enchantment, l);

        this.setItem(kill, slot, item.build());
    }

    private ItemBuilder getRandomBuilder(Kit kit, int slot) {
        switch (slot) {
            case 0:
                return kit.getFirstItem().clone();
            case 1:
                return kit.getHelmet().clone();
            case 2:
                return kit.getChestplate().clone();
            case 3:
                return kit.getLeggings().clone();
            case 4:
                return kit.getBoots().clone();
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
