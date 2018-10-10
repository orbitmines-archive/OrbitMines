package com.orbitmines.spigot.servers.survival.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.gui.PrismSolarShopGUI;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;

public class SurvivalPrismSolarShopGUI extends PrismSolarShopGUI {

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        add(3, 1, new ShopItem(omp, Currency.PRISMS, 200, new ItemBuilder(Material.STONE_HOE, 1, "§a§lSurvival Package").addFlag(ItemFlag.HIDE_ATTRIBUTES), "§9§l100 Claimblocks", "§2§l50 Credits") {
            @Override
            public void give(OMPlayer player) {
                SurvivalPlayer omp = (SurvivalPlayer) player;
                omp.addClaimBlocks(100);
                omp.addEarthMoney(50);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §9§l100 Claimblocks §7en §2§l50 Credits§7 ontvangen.", "You have received §9§l100 Claimblocks §7and §2§l50 Credits§7.");
            }
        });

        add(3, 2, new ShopItem(omp, Currency.PRISMS, 100, new ItemBuilder(Material.SCUTE, 1, "§2§lCredits Package"), "§2§l200 Credits") {
            @Override
            public void give(OMPlayer player) {
                SurvivalPlayer omp = (SurvivalPlayer) player;
                omp.addEarthMoney(200);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §2§l200 Credits§7 ontvangen.", "You have received §2§l200 Credits§7.");
            }
        });

        if (!omp.warpSlotPrisms())
            add(3, 3, new ShopItem(omp, Currency.PRISMS, 30000, new ItemBuilder(Material.WRITABLE_BOOK, 1, Warp.COLOR.getChatColor() + "§lWarp Package"), "§9§lPrisms Warp Slot") {
                @Override
                public void give(OMPlayer player) {
                    SurvivalPlayer omp = (SurvivalPlayer) player;
                    omp.setWarpSlotPrisms(true);

                    omp.sendMessage("Shop", Color.BLUE, "Je hebt de §9§lPrisms Warp Slot§7 ontgrendeld.", "You have unlocked the §9§lPrisms Warp Slot§7.");
                }
            });
        else
            add(3, 3, new EmptyItemInstance(new ItemBuilder(Material.BOOK, 1, "§c§lWarp Package", "§7§o" + omp.lang("Gekocht", "Bought")).build()));

        add(3, 4, new ShopItem(omp, Currency.PRISMS, 2500, new ItemBuilder(Material.SPRUCE_DOOR, 1, "§6§lHome Package"), "§6§l1 Home") {
            @Override
            public void give(OMPlayer player) {
                SurvivalPlayer omp = (SurvivalPlayer) player;
                omp.addExtraHomes(1);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §6§l1 Home§7 ontvangen.", "You have received §6§l1 Home§7.");
            }
        });

        add(3, 5, new ShopItem(omp, Currency.SOLARS, 1000, new PlayerSkullBuilder(() -> omp.getName(true), 1, "§a§lYour Skull Package"), "§a§l1x " + omp.getName(true) + "'s Skull") {
            @Override
            public void give(OMPlayer omp) {
                omp.getPlayer().getInventory().addItem(new PlayerSkullBuilder(() -> omp.getName(true), 1).build());

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §a§l1x " + omp.getName(true) + "'s Skull§7 ontvangen.", "You have received §a§l1x " + omp.getName(true) + "'s Skull§7.");
            }

            @Override
            public boolean canReceive(OMPlayer omp) {
                return omp.getInventory().firstEmpty() != -1;
            }

            @Override
            public void cantReceive(OMPlayer omp) {
                omp.sendMessage("Shop", Color.RED, "Je inventory zit vol!", "Your inventory is full!");
                omp.playSound(Sound.ENTITY_ENDERMAN_SCREAM);
            }
        });

        add(3, 6, new ShopItem(omp, Currency.SOLARS, 50, new ItemBuilder(Material.ENDER_EYE, 1, "§6§lBack Charges Package"), "§6§l10 Back Charges") {
            @Override
            public void give(OMPlayer player) {
                SurvivalPlayer omp = (SurvivalPlayer) player;
                omp.addBackCharges(10);

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §6§l10 Back Charges§7 ontvangen.", "You have received §6§l10 Back Charges§7.");
            }
        });

        add(3, 7, new ShopItem(omp, Currency.SOLARS, 1500, new ItemBuilder(Material.DIAMOND_PICKAXE, 1, "§5§lSpawner Miner Package").addFlag(ItemFlag.HIDE_ATTRIBUTES), "§5§l1x Spawner Miner") {
            @Override
            public void give(OMPlayer omp) {
                omp.getPlayer().getInventory().addItem(Survival.SPAWNER_MINER.build());

                omp.sendMessage("Shop", Color.BLUE, "Je hebt §5§l1x Spawner Miner§7 ontvangen.", "You have received §5§l1x Spawner Miner§7.");
            }

            @Override
            public boolean canReceive(OMPlayer omp) {
                return omp.getInventory().firstEmpty() != -1;
            }

            @Override
            public void cantReceive(OMPlayer omp) {
                omp.sendMessage("Shop", Color.RED, "Je inventory zit vol!", "Your inventory is full!");
                omp.playSound(Sound.ENTITY_ENDERMAN_SCREAM);
            }
        });

        return super.onOpen(omp);
    }
}
