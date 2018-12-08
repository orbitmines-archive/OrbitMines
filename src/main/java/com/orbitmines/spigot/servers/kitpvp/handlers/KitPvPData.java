package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPKitStats;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.KitArcher;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.KitKnight;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.KitSoldier;

import java.util.*;
import java.util.UUID;

import static com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers.*;

public class KitPvPData extends Data {

    private int coins;
    private int experience;

    private int kills;
    private int deaths;

    private int bestStreak;

    private double damageDealt;

    private long lastSelectedId;
    private int lastSelectedLevel;

    private Map<Long, KitData> kitData;

    public KitPvPData(UUID uuid) {
        super(Table.KITPVP_PLAYERS, Type.KITPVP, uuid);

        coins = 0;
        experience = 0;

        kills = 0;
        deaths = 0;

        bestStreak = 0;

        damageDealt = 0D;

        lastSelectedId = 0;
        lastSelectedLevel = 1;

        kitData = new HashMap<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { TableKitPvPPlayers.UUID }, new Where(TableKitPvPPlayers.UUID, getUUID().toString()))) {
            Database.get().insert(table, uuid.toString(), coins + "", experience + "", kills + "", deaths + "", bestStreak + "", damageDealt + "", lastSelectedId + "", lastSelectedLevel + "");
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    COINS,
                    EXPERIENCE,
                    KILLS,
                    DEATHS,
                    BEST_STREAK,
                    DAMAGE_DEALT,
                    LAST_SELECTED_ID,
                    LAST_SELECTED_LEVEL
            }, new Where(TableKitPvPPlayers.UUID, getUUID().toString()));

            coins = Integer.parseInt(values.get(COINS));
            experience = Integer.parseInt(values.get(EXPERIENCE));

            kills = Integer.parseInt(values.get(KILLS));
            deaths = Integer.parseInt(values.get(DEATHS));

            bestStreak = Integer.parseInt(values.get(BEST_STREAK));

            damageDealt = Double.parseDouble(values.get(DAMAGE_DEALT));

            lastSelectedId = Long.parseLong(values.get(LAST_SELECTED_ID));
            lastSelectedLevel = Integer.parseInt(values.get(LAST_SELECTED_LEVEL));
        }
    }

    /*
        Coins
     */

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;

        updateCoins();
    }

    public void removeCoins(int amount) {
        coins -= amount;

        updateCoins();
    }

    private void updateCoins() {
        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.COINS, this.coins), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));
    }

    /*
        Experience
     */

    public int getExperience() {
        return experience;
    }

    public void addExperience(int amount) {
        experience += amount;

        updateExperience();
    }

    private void updateExperience() {
        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.EXPERIENCE, this.experience), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));
    }

    /*
        Kills
     */

    public int getKills() {
        return kills;
    }

    public void addKill(long kitId) {
        /* Total Stats */
        kills++;
        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.KILLS, this.kills), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));

        /* Kit Stats */
        KitData data = getKitData(kitId);
        data.addKill();
    }

    /*
        Deaths
     */

    public int getDeaths() {
        return deaths;
    }

    public void addDeath(long kitId) {
        /* Total Stats */
        deaths++;
        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.DEATHS, this.deaths), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));

        /* Kit Stats */
        KitData data = getKitData(kitId);
        data.addDeath();
    }

    /*
        BestStreak
     */

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(long kitId, int bestStreak, boolean total) {
        if (total) {
            /* Total Stats */
            this.bestStreak = bestStreak;
            Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.BEST_STREAK, this.bestStreak), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));
        }

        /* Kit Stats */
        KitData data = getKitData(kitId);
        data.setBestStreak(bestStreak);
    }

    /*
        DamageDealt
     */

    public double getDamageDealt() {
        return damageDealt;
    }

    public void addDamageDealt(long kitId, double damageDealt) {
        /* Total Stats */
        this.damageDealt += damageDealt;

        /* Kit Stats */
        KitData data = getKitData(kitId);
        data.addDamageDealt(damageDealt);
    }

    public void updateDamageDealt(long kitId) {
        /* Total Stats */
        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.DAMAGE_DEALT, this.damageDealt), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));

        /* Kit Stats */
        KitData data = getKitData(kitId);
        data.updateDamageDealt();
    }

    /*
        Last Selected
     */

    public long getLastSelectedId() {
        return lastSelectedId;
    }

    public void setLastSelectedId(long lastSelectedId) {
        this.lastSelectedId = lastSelectedId;

        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.LAST_SELECTED_ID, this.lastSelectedId), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));
    }

    public int getLastSelectedLevel() {
        return lastSelectedLevel;
    }

    public void setLastSelectedLevel(int lastSelectedLevel) {
        this.lastSelectedLevel = lastSelectedLevel;

        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.LAST_SELECTED_LEVEL, this.lastSelectedLevel), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));
    }

    /*
        KitData
     */

    public KitData getKitData(long kitId) {
        if (kitData.containsKey(kitId))
            return kitData.get(kitId);

        KitData data = new KitData(kitId);
        data.load();

        this.kitData.put(kitId, data);

        return data;
    }

    public List<KitData> getAllKitData(Long... kitIds) {
        List<KitData> list = new ArrayList<>();

        //TODO ADD KIT IDS TO WHERE IN () CLAUSE
        List<Long> ids = null;
        if (kitIds != null && kitIds.length != 0)
            ids = Arrays.asList(kitIds);
        //

        for (Map<Column, String> entry : Database.get().getEntries(Table.KITPVP_KIT_STATS, TableKitPvPKitStats.KIT_ID, new Where(TableKitPvPKitStats.UUID, getUUID().toString()))) {
            long kitId = Long.parseLong(entry.get(TableKitPvPKitStats.KIT_ID));
            if (ids == null || ids.contains(kitId))
                list.add(getKitData(kitId));
        }

        return list;
    }

    public class KitData {

        private final long kitId;

        private int unlockedLevel;

        private int kills;
        private int deaths;

        private int bestStreak;

        private double damageDealt;

        public KitData(long kitId) {
            this.kitId = kitId;

            /* Unlock Knight, Archer & Soldier. */
            this.unlockedLevel = this.kitId == KitKnight.ID || this.kitId == KitArcher.ID || this.kitId == KitSoldier.ID ? 1 : 0;

            this.kills = 0;
            this.deaths = 0;

            this.bestStreak = 0;

            this.damageDealt = 0D;
        }

        public void load() {
            if (!Database.get().contains(Table.KITPVP_KIT_STATS, new Column[] { TableKitPvPKitStats.UUID }, new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId))) {
                Database.get().insert(Table.KITPVP_KIT_STATS, uuid.toString(), kitId + "", unlockedLevel + "", kills + "", deaths + "", bestStreak + "", damageDealt + "");
            } else {
                Map<Column, String> values = Database.get().getValues(Table.KITPVP_KIT_STATS, new Column[] {
                        TableKitPvPKitStats.UNLOCKED_LEVEL,
                        TableKitPvPKitStats.KILLS,
                        TableKitPvPKitStats.DEATHS,
                        TableKitPvPKitStats.BEST_STREAK,
                        TableKitPvPKitStats.DAMAGE_DEALT
                }, new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId));

                unlockedLevel = Integer.parseInt(values.get(TableKitPvPKitStats.UNLOCKED_LEVEL));

                kills = Integer.parseInt(values.get(TableKitPvPKitStats.KILLS));
                deaths = Integer.parseInt(values.get(TableKitPvPKitStats.DEATHS));

                bestStreak = Integer.parseInt(values.get(TableKitPvPKitStats.BEST_STREAK));

                damageDealt = Double.parseDouble(values.get(TableKitPvPKitStats.DAMAGE_DEALT));
            }
        }

        public int getUnlockedLevel() {
            return unlockedLevel;
        }

        public void setUnlockedLevel(int unlockedLevel) {
            this.unlockedLevel = unlockedLevel;

            Database.get().update(Table.KITPVP_KIT_STATS, new Set(TableKitPvPKitStats.UNLOCKED_LEVEL, this.unlockedLevel), new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId));
        }

        public int getKills() {
            return kills;
        }

        public void addKill() {
            kills++;

            Database.get().update(Table.KITPVP_KIT_STATS, new Set(TableKitPvPKitStats.KILLS, this.kills), new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId));
        }

        public int getDeaths() {
            return deaths;
        }

        public void addDeath() {
            deaths++;

            Database.get().update(Table.KITPVP_KIT_STATS, new Set(TableKitPvPKitStats.DEATHS, this.deaths), new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId));
        }

        public int getBestStreak() {
            return bestStreak;
        }

        public void setBestStreak(int bestStreak) {
            this.bestStreak = bestStreak;

            Database.get().update(Table.KITPVP_KIT_STATS, new Set(TableKitPvPKitStats.BEST_STREAK, this.bestStreak), new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId));
        }

        public double getDamageDealt() {
            return damageDealt;
        }

        public void addDamageDealt(double damageDealt) {
            this.damageDealt += damageDealt;
        }

        public void updateDamageDealt() {
            Database.get().update(Table.KITPVP_KIT_STATS, new Set(TableKitPvPKitStats.DAMAGE_DEALT, this.damageDealt), new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId));
        }
    }
}
