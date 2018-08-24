package com.orbitmines.spigot.api;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.tables.TablePeriodLoot;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.MobEggBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public enum PeriodLoot {

    DAILY(TimeUnit.DAYS.toSeconds(1), TablePeriodLoot.DAILY, new MobEggBuilder(Mob.CHICKEN, 1), new ItemBuilder(Material.EGG), new Message("Dagelijkse Beloningen", "Daily Rewards"), new Message("§7- §9§l250 Prisms")) {
        @Override
        public void receive(OMPlayer omp) {
            omp.addPrisms(250);
        }
    },
    MONTHLY(TimeUnit.DAYS.toSeconds(30), TablePeriodLoot.MONTHLY, new MobEggBuilder(Mob.PUFFERFISH, 1), new ItemBuilder(Material.EGG), new Message("Maandelijkse Beloningen", "Monthly Rewards"), new Message("§7- §9§l2,500 Prisms"), new Message("§7- §e§l50 Solars")) {
        @Override
        public void receive(OMPlayer omp) {
            omp.addPrisms(2500);
            omp.addSolars(100);
        }
    },
    MONTHLY_VIP(TimeUnit.DAYS.toSeconds(30), TablePeriodLoot.MONTHLY_VIP, null, new ItemBuilder(Material.EGG), new Message("Maandelijkse Rank Beloningen", "Monthly Rank Rewards")) {
        @Override
        public ItemBuilder getClaimable(OMPlayer omp) {
            switch (omp.getVipRank()) {

                case IRON:
                    return new MobEggBuilder(Mob.POLAR_BEAR, 1);
                case GOLD:
                    return new MobEggBuilder(Mob.BLAZE, 1);
                case DIAMOND:
                    return new MobEggBuilder(Mob.VEX, 1);
                case EMERALD:
                    return new MobEggBuilder(Mob.SLIME, 1);
                default:
                    return new MobEggBuilder(Mob.ENDERMAN, 1);
            }
        }

        @Override
        public Message[] getDescription(OMPlayer omp) {
            return new Message[] {
                    new Message(omp.getVipRank() == VipRank.IRON ? VipRank.IRON.getDisplayName() : "§8§l" + VipRank.IRON.getName()),
                    new Message(omp.getVipRank() == VipRank.IRON ? "§7- §e§l150 Solars" : "§8- §l150 Solars"),
                    new Message(omp.getVipRank() == VipRank.GOLD ? VipRank.GOLD.getDisplayName() : "§8§l" + VipRank.GOLD.getName()),
                    new Message(omp.getVipRank() == VipRank.GOLD ? "§7- §e§l300 Solars" : "§8- §l300 Solars"),
                    new Message(omp.getVipRank() == VipRank.DIAMOND ? VipRank.DIAMOND.getDisplayName() : "§8§l" + VipRank.DIAMOND.getName()),
                    new Message(omp.getVipRank() == VipRank.DIAMOND ? "§7- §e§l600 Solars" : "§8- §l600 Solars"),
                    new Message(omp.getVipRank() == VipRank.EMERALD ? VipRank.EMERALD.getDisplayName() : "§8§l" + VipRank.EMERALD.getName()),
                    new Message(omp.getVipRank() == VipRank.EMERALD ? "§7- §e§l1000 Solars" : "§8- §l1000 Solars"),
            };
        }

        @Override
        public void receive(OMPlayer omp) {
            switch (omp.getVipRank()) {

                case NONE:
                    break;
                case IRON:
                    omp.addSolars(150);
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
    },
    SURVIVAL_BACK_CHARGES(Server.SURVIVAL, TimeUnit.DAYS.toSeconds(30), TablePeriodLoot.SURVIVAL_BACK_CHARGES, new ItemBuilder(Material.ENDER_EYE), new ItemBuilder(Material.EGG), new Message("Maandelijkse Back Charges", "Monthly Back Charges")) {

        @Override
        public Message[] getDescription(OMPlayer omp) {
            return new Message[] {
                    new Message(omp.getVipRank() == VipRank.IRON ? VipRank.IRON.getDisplayName() : "§8§l" + VipRank.IRON.getName()),
                    new Message(omp.getVipRank() == VipRank.IRON ? "§7- §6§l50 Back Charges" : "§8- §l50 Back Charges"),
                    new Message(omp.getVipRank() == VipRank.GOLD ? VipRank.GOLD.getDisplayName() : "§8§l" + VipRank.GOLD.getName()),
                    new Message(omp.getVipRank() == VipRank.GOLD ? "§7- §6§l125 Back Charges" : "§8- §l125 Back Charges"),
                    new Message(omp.getVipRank() == VipRank.DIAMOND ? VipRank.DIAMOND.getDisplayName() : "§8§l" + VipRank.DIAMOND.getName()),
                    new Message(omp.getVipRank() == VipRank.DIAMOND ? "§7- §6§200 Back Charges" : "§8- §l200 Back Charges"),
                    new Message(omp.getVipRank() == VipRank.EMERALD ? VipRank.EMERALD.getDisplayName() : "§8§l" + VipRank.EMERALD.getName()),
                    new Message(omp.getVipRank() == VipRank.EMERALD ? "§7- §6§l300 Back Charges" : "§8- §l300 Back Charges"),
            };
        }

        @Override
        public void receive(OMPlayer player) {
            SurvivalPlayer omp = (SurvivalPlayer) player;

            switch (omp.getVipRank()) {

                case NONE:
                    break;
                case IRON:
                    omp.addBackCharges(50);
                    break;
                case GOLD:
                    omp.addBackCharges(125);
                    break;
                case DIAMOND:
                    omp.addBackCharges(200);
                    break;
                case EMERALD:
                    omp.addBackCharges(300);
                    break;
            }
        }
    },
    SURVIVAL_SPAWNER_ITEM(Server.SURVIVAL, TimeUnit.DAYS.toSeconds(30), TablePeriodLoot.SURVIVAL_SPAWNER_ITEM, new ItemBuilder(Material.DIAMOND_PICKAXE).addFlag(ItemFlag.HIDE_ATTRIBUTES), new ItemBuilder(Material.EGG), new Message("Maandelijkse Spawner Miner", "Monthly Spawner Miner")) {

        @Override
        public Message[] getDescription(OMPlayer omp) {
            return new Message[] {
                    new Message(omp.getVipRank() == VipRank.EMERALD ? VipRank.EMERALD.getDisplayName() : "§8§l" + VipRank.EMERALD.getName()),
                    new Message(omp.getVipRank() == VipRank.EMERALD ? "§7- §5§l1 Spawner Miner" : "§8- §l1 Spawner Miner"),
            };
        }

        @Override
        public void receive(OMPlayer player) {
            SurvivalPlayer omp = (SurvivalPlayer) player;

            switch (omp.getVipRank()) {

                case EMERALD:
                    omp.getInventory().addItem(Survival.SPAWNER_MINER.build());
                    break;
            }
        }
    };

    private final Server server;

    private final long delay;
    private final Column column;

    private final ItemBuilder claimable;
    private final ItemBuilder claimed;

    private final Message title;
    private final Message[] description;

    PeriodLoot(long delay, Column column, ItemBuilder claimable, ItemBuilder claimed, Message title, Message... description) {
        this(null, delay, column, claimable, claimed, title, description);
    }

    PeriodLoot(Server server, long delay, Column column, ItemBuilder claimable, ItemBuilder claimed, Message title, Message... description) {
        this.server = server;
        this.delay = delay;
        this.column = column;
        this.claimable = claimable;
        this.claimed = claimed;
        this.title = title;
        this.description = description;
    }

    public Server getServer() {
        return server;
    }

    public long getDelay() {
        return delay;
    }

    public Column getColumn() {
        return column;
    }

    public ItemBuilder getClaimable(OMPlayer omp) {
        return claimable.clone();
    }

    public ItemBuilder getClaimed(OMPlayer omp) {
        return claimed.clone();
    }

    public Message getTitle(OMPlayer omp) {
        return title;
    }

    public Message[] getDescription(OMPlayer omp) {
        return description;
    }

    public void receive(OMPlayer omp) {
        throw new IllegalStateException();
    }

    public static List<PeriodLoot> from(Server server){
        List<PeriodLoot> list = new ArrayList<>();

        for (PeriodLoot loot : values()) {
            if (loot.server == server)
                list.add(loot);
        }

        return list;
    }
}
