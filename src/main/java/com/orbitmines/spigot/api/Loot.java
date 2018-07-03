package com.orbitmines.spigot.api;
/*
 * OrbitMines - @author Fadi Shawki - 15-6-2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.LootData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import org.bukkit.Material;
import org.bukkit.Sound;

public enum Loot {

    DONATION() {
        @Override
        public Server getServer(int count) {
            return Donation.getById(count).getServer();
        }

        @Override
        public String getDisplayName(int count) {
            return Donation.getById(count).getTitle();
        }

        @Override
        public ItemBuilder getIcon(int count) {
            switch(Donation.getById(count)) {

                case VIP_IRON:
                    return new ItemBuilder(Material.IRON_INGOT);
                case VIP_GOLD:
                case VIP_UPGRADE_IRON_GOLD:
                    return new ItemBuilder(Material.GOLD_INGOT);
                case VIP_DIAMOND:
                case VIP_UPGRADE_IRON_DIAMOND:
                case VIP_UPGRADE_GOLD_DIAMOND:
                    return new ItemBuilder(Material.DIAMOND);
                case VIP_EMERALD:
                case VIP_UPGRADE_IRON_EMERALD:
                case VIP_UPGRADE_GOLD_EMERALD:
                case VIP_UPGRADE_DIAMOND_EMERALD:
                    return new ItemBuilder(Material.EMERALD);
                case SOLARS_1000:
                case SOLARS_2500:
                case SOLARS_7500:
                    return new ItemBuilder(Material.DOUBLE_PLANT);
                case SURVIVAL_CLAIMBLOCKS_10000:
                case SURVIVAL_CLAIMBLOCKS_25000:
                case SURVIVAL_CLAIMBLOCKS_75000:
                case SURVIVAL_CLAIMBLOCKS_50000:
                    return new ItemBuilder(Material.STONE_HOE);
                case SURVIVAL_HOMES_25:
                case SURVIVAL_HOMES_100:
                    return new ItemBuilder(Material.SPRUCE_DOOR_ITEM);
                case SURVIVAL_WARP_1:
                    return new ItemBuilder(Material.ENDER_PEARL);
            }
            return new ItemBuilder(Material.STONE);
        }

        @Override
        public void onInteract(OMPlayer omp, Rarity rarity, String description, int count) {
            Donation donation = Donation.getById(count);
            if (donation.getUpgradedFrom() != null && omp.getVipRank() != donation.getUpgradedFrom()) {
                omp.sendMessage("Loot", Color.RED, "§7Je kan dit niet gebruiken omdat je geen " + donation.getUpgradedFrom().getDisplayName() + "§7 bent.", "§7You can't use this because you don't have the " + donation.getUpgradedFrom().getDisplayName() + "§7 rank.");
                return;
            }

            omp.getPlayer().closeInventory();

            LootData data = ((LootData) omp.getData(Data.Type.LOOT));
            data.remove(this, rarity, description);

            omp.playSound(Sound.ENTITY_PLAYER_LEVELUP);

            switch (donation) {

                case VIP_IRON:
                case VIP_GOLD:
                case VIP_DIAMOND:
                case VIP_EMERALD:
                case VIP_UPGRADE_IRON_GOLD:
                case VIP_UPGRADE_IRON_DIAMOND:
                case VIP_UPGRADE_IRON_EMERALD:
                case VIP_UPGRADE_GOLD_DIAMOND:
                case VIP_UPGRADE_GOLD_EMERALD:
                case VIP_UPGRADE_DIAMOND_EMERALD: {
                    VipRank rank = donation.getRank();

                    data.add(Loot.SOLARS, Rarity.LEGENDARY, donation.getTitle().replace("§l", "§l§o") + " §e§l§oSolars", rank.getSolars(omp.getVipRank()));
                    omp.setVipRank(rank);
                    break;
                }
                case SOLARS_1000:
                case SOLARS_2500:
                case SOLARS_7500: {
                    int solars = 0;
                    switch (donation) {
                        case SOLARS_1000:
                            solars = 1000;
                            break;
                        case SOLARS_2500:
                            solars = 2500;
                            break;
                        case SOLARS_7500:
                            solars = 7500;
                            break;
                    }

                    omp.addSolars(solars);

                    omp.sendMessage("Solars", Color.BLUE, "§7Je hebt §e§l" + NumberUtils.locale(solars) + " Solars§7 ontvangen.", "§7You have received §e§l" + NumberUtils.locale(solars) + " Solars§7.");
                    break;
                }
                case SURVIVAL_CLAIMBLOCKS_10000:
                case SURVIVAL_CLAIMBLOCKS_25000:
                case SURVIVAL_CLAIMBLOCKS_75000:
                case SURVIVAL_CLAIMBLOCKS_50000: {
                    int blocks = 0;
                    switch (donation) {
                        case SURVIVAL_CLAIMBLOCKS_10000:
                            blocks = 10000;
                            break;
                        case SURVIVAL_CLAIMBLOCKS_25000:
                            blocks = 25000;
                            break;
                        case SURVIVAL_CLAIMBLOCKS_75000:
                            blocks = 75000;
                            break;
                        case SURVIVAL_CLAIMBLOCKS_50000:
                            blocks = 50000;
                            break;
                    }

                    SurvivalPlayer somp = (SurvivalPlayer) omp;

                    somp.addClaimBlocks(blocks);

                    omp.sendMessage("Claimblocks", Color.BLUE, "§7Je hebt §9§l" + NumberUtils.locale(blocks) + " Claimblocks§7 ontvangen.", "§7You have received §9§l" + NumberUtils.locale(blocks) + " Claimblocks§7.");
                    break;
                }
                case SURVIVAL_HOMES_25:
                case SURVIVAL_HOMES_100: {
                    int homes = 0;
                    switch (donation) {
                        case SURVIVAL_HOMES_25:
                            homes = 25;
                            break;
                        case SURVIVAL_HOMES_100:
                            homes = 100;
                            break;
                    }

                    SurvivalPlayer somp = (SurvivalPlayer) omp;

                    somp.addExtraHomes(somp.getExtraHomes());

                    omp.sendMessage("Homes", Color.BLUE, "§7Je hebt §6§l" + NumberUtils.locale(homes) + " Homes§7 ontvangen.", "§7You have received §6§l" + NumberUtils.locale(homes) + " Homes§7.");
                    break;
                }
                case SURVIVAL_WARP_1: {
                    SurvivalPlayer somp = (SurvivalPlayer) omp;

                    somp.setWarpSlotShop(true);

                    omp.sendMessage("Warp", Color.BLUE, "§7Je hebt de " + Warp.COLOR.getChatColor() + "§lOrbitMines Shop §7§lSlot§7 ontgrendeld.", "§7You have unlocked the " + Warp.COLOR.getChatColor() + "§lOrbitMines Shop §7§lSlot§7.");
                    break;
                }
            }
        }
    },
    BUYCRAFT_VOUCHER() {
        @Override
        public String getDisplayName(int count) {
            return "§3§l" + count + "€ OrbitMines Shop Voucher";
        }

        @Override
        public ItemBuilder getIcon(int count) {
            return new ItemBuilder(Material.PAPER);
        }

        @Override
        public void onInteract(OMPlayer omp, Rarity rarity, String description, int count) {
            //TODO Open OM Shop
        }
    },
    PRISMS() {
        @Override
        public String getDisplayName(int count) {
            return "§9§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Prism" : "Prisms");
        }

        @Override
        public ItemBuilder getIcon(int count) {
            return new ItemBuilder(Material.PRISMARINE_SHARD);
        }

        @Override
        public void onInteract(OMPlayer omp, Rarity rarity, String description, int count) {
            omp.getPlayer().closeInventory();

            LootData data = ((LootData) omp.getData(Data.Type.LOOT));
            data.remove(this, rarity, description);

            omp.addPrisms(count);

            omp.playSound(Sound.ENTITY_PLAYER_LEVELUP);
            omp.sendMessage("Prisms", Color.BLUE, "§7Je hebt §9§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Prism" : "Prisms") + "§7 ontvangen.", "§7You have received §9§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Prism" : "Prisms") + "§7.");
        }
    },
    SOLARS() {
        @Override
        public String getDisplayName(int count) {
            return "§e§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Solar" : "Solars");
        }

        @Override
        public ItemBuilder getIcon(int count) {
            return new ItemBuilder(Material.DOUBLE_PLANT);//TODO
        }

        @Override
        public void onInteract(OMPlayer omp, Rarity rarity, String description, int count) {
            omp.getPlayer().closeInventory();

            LootData data = ((LootData) omp.getData(Data.Type.LOOT));
            data.remove(this, rarity, description);

            omp.addSolars(count);

            omp.playSound(Sound.ENTITY_PLAYER_LEVELUP);
            omp.sendMessage("Solars", Color.BLUE, "§7Je hebt §e§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Solar" : "Solars") + "§7 ontvangen.", "§7You have received §e§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Solar" : "Solars") + "§7.");
        }
    };

    private Server server;

    Loot() {
        this(null);
    }

    Loot(Server server) {
        this.server = server;
    }

    public Server getServer(int count) {
        return server;
    }

    public String getDisplayName(int count) {
        throw new IllegalStateException();
    }

    public ItemBuilder getIcon(int count) {
        throw new IllegalStateException();
    }

    public void onInteract(OMPlayer omp, Rarity rarity, String description, int count) {
        throw new IllegalStateException();
    }
}
