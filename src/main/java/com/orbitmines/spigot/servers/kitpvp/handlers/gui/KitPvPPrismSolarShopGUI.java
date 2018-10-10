package com.orbitmines.spigot.servers.kitpvp.handlers.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.gui.PrismSolarShopGUI;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;

public class KitPvPPrismSolarShopGUI extends PrismSolarShopGUI {

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        add(3, 2, new ShopItem(omp, Currency.PRISMS, 100, new ItemBuilder(Material.GOLD_NUGGET, 1, "§6§lSmall Coin Package"), "§6§l200 Coins") {
            @Override
            public void give(OMPlayer player) {
                KitPvPPlayer omp = (KitPvPPlayer) player;
                omp.addCoins(200, false);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §6§l200 Coins§7 ontvangen.", "You have received §6§l200 Coins§7.");
            }
        });

        add(3, 4, new ShopItem(omp, Currency.PRISMS, 500, new ItemBuilder(Material.GOLD_NUGGET, 5, "§6§lLarge Coin Package"), "§6§l1,000 Coins") {
            @Override
            public void give(OMPlayer player) {
                KitPvPPlayer omp = (KitPvPPlayer) player;
                omp.addCoins(1000, false);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §6§l1,000 Coins§7 ontvangen.", "You have received §6§l1,000 Coins§7.");
            }
        });

        add(3, 6, new ShopItem(omp, Currency.PRISMS, 1000, new ItemBuilder(Material.GOLD_NUGGET, 10, "§6§lHuge Coin Package"), "§6§l2,000 Coins") {
            @Override
            public void give(OMPlayer player) {
                KitPvPPlayer omp = (KitPvPPlayer) player;
                omp.addCoins(2000, false);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §6§l2,000 Coins§7 ontvangen.", "You have received §6§l2,000 Coins§7.");
            }
        });

        return super.onOpen(omp);
    }
}
