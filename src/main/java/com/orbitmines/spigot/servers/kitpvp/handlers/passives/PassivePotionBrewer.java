package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitPotionItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PassivePotionBrewer implements Passive.Handler<PlayerDeathEvent> {

    @Override
    public void trigger(PlayerDeathEvent event, int level) {
        /* There's a chance of brewing a potion, otherwise move on */
        if (Math.random() >= getChance(level))
            return;

        Player entity = event.getEntity();

        Player killer = entity.getKiller();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(killer);

        Kit kit = omp.getSelectedKit().getKit();
        List<ItemBuilder> potions = kit.getItems(Material.POTION, Material.LINGERING_POTION, Material.SPLASH_POTION);

        /* No potions from current selected kit */
        if (potions.size() == 0)
            return;

        KitPotionItemBuilder item = (KitPotionItemBuilder) RandomUtils.randomFrom(potions);
        ItemStack potion = item.build();
        int slot = killer.getInventory().first(potion);
        if (slot == -1) {
            killer.getInventory().addItem(potion);
        } else {
            potion = killer.getInventory().getItem(slot);

            /* Already reached max capacity for this item */
            if (potion.getAmount() >= 64)
                return;

            potion.setAmount(potion.getAmount() + 1);
            killer.getInventory().setItem(slot, potion);

            PlayerUtils.updateInventory(killer);
        }
        /* Build Item Hologram */
        FloatingItem hologram = new FloatingItem(item, entity.getLocation());
        hologram.addLine(() -> Passive.POTION_BREWER.getColor().getChatColor() + "§l" + Passive.POTION_BREWER.getName(), false);

        PotionBuilder builder = item.getPotionBuilders().get(0);
        hologram.addLine(() -> "§e§o+ " + ItemUtils.getName(builder.getType()) + " " + NumberUtils.toRoman(builder.getAmplifier() + 1) + " Potion", false);
        hologram.create(killer);

        new BukkitRunnable() {
            @Override
            public void run() {
                hologram.destroy();
            }
        }.runTaskLater(OrbitMines.getInstance(), 60L);
    }

    public double getChance(int level) {
        switch (level) {
            case 1:
                return 0.125;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }
}
