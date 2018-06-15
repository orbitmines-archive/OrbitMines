package com.orbitmines.spigot.api;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public enum PeriodLoot {

    DAILY(TimeUnit.DAYS.toSeconds(1), new ItemBuilder(Material.STORAGE_MINECART), new ItemBuilder(Material.MINECART), new Message("Dagelijkse Beloningen", "Daily Rewards"), new Message("§9§l250 Prisms")) {
        @Override
        public void receive(OMPlayer omp) {
            omp.addPrisms(250);
        }
    },
    MONTHLY(TimeUnit.DAYS.toSeconds(30), new ItemBuilder(Material.POWERED_MINECART), new ItemBuilder(Material.MINECART), new Message("Maandelijkse Beloningen", "Monthly Rewards"), new Message("§9§l2,500 Prisms"), new Message("§e§l100 Solars")) {
        @Override
        public void receive(OMPlayer omp) {
            omp.addPrisms(2500);
            omp.addSolars(100);
        }
    },
    MONTHLY_VIP(TimeUnit.DAYS.toSeconds(30), new ItemBuilder(Material.EXPLOSIVE_MINECART), new ItemBuilder(Material.MINECART), new Message("Maandelijkse Rank Beloningen", "Monthly Rank Rewards")) {
        @Override
        public void receive(OMPlayer omp) {
            switch (omp.getVipRank()) {

                case NONE:
                    break;
                case IRON:
                    omp.addSolars(100);
                    break;
                case GOLD:
                    omp.addSolars(300);
                    break;
                case DIAMOND:
                    omp.addSolars(600);
                    break;
                case EMERALD:
                    omp.addSolars(1000);
                    break;
            }
        }
    };

    private final long delay;

    private final ItemBuilder claimable;
    private final ItemBuilder claimed;

    private final Message title;
    private final Message[] description;

    PeriodLoot(long delay, ItemBuilder claimable, ItemBuilder claimed, Message title, Message... description) {
        this.delay = delay;
        this.claimable = claimable;
        this.claimed = claimed;
        this.title = title;
        this.description = description;
    }

    public long getDelay() {
        return delay;
    }

    public ItemBuilder getClaimable() {
        return claimable;
    }

    public ItemBuilder getClaimed() {
        return claimed;
    }

    public Message getTitle() {
        return title;
    }

    public Message[] getDescription() {
        return description;
    }

    public void receive(OMPlayer omp) {
        throw new IllegalStateException();
    }
}
