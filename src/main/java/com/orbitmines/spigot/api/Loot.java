package com.orbitmines.spigot.api;
/*
 * OrbitMines - @author Fadi Shawki - 15-6-2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.LootData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;

public enum Loot {

//    DONATION() {
//        @Override
//        public Server getServer(int count) {
//            return Donation.getById(count).getServer();
//        }
//
//        @Override
//        public String getDisplayName(int count) {
//
//        }
//
//        @Override
//        public ItemBuilder getIcon(int count) {
//
//        }
//
//        @Override
//        public void onInteract(OMPlayer omp, Rarity rarity, String description, int count) {
//
//        }
//    },
    BUYCRAFT_VOUCHER() {
        @Override
        public String getDisplayName(int count) {
            return "§3§l" + count + "€ OrbitMines Shop Voucher";
        }

        @Override
        public ItemBuilder getIcon(int count) {
            return new ItemBuilder(Material.PAPER).clone();
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
            return new ItemBuilder(Material.PRISMARINE_SHARD).clone();
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
            return new ItemBuilder(Material.DOUBLE_PLANT).clone();//TODO
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
