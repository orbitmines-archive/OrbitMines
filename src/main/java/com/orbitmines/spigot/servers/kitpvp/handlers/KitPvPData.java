package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPKitStats;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers;
import com.orbitmines.spigot.api.handlers.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers.*;

public class KitPvPData extends Data {

    private int coins;
    private int experience;

    private int kills;
    private int deaths;

    private Map<Long, KitData> kitData;

    public KitPvPData(UUID uuid) {
        super(Table.KITPVP_PLAYERS, Type.KITPVP, uuid);

        coins = 0;
        experience = 0;

        kills = 0;
        deaths = 0;

        kitData = new HashMap<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { TableKitPvPPlayers.UUID }, new Where(TableKitPvPPlayers.UUID, getUUID().toString()))) {
            Database.get().insert(table, uuid.toString(), coins + "", experience + "", kills + "", deaths + "");
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    COINS,
                    EXPERIENCE,
                    KILLS,
                    DEATHS
            }, new Where(TableKitPvPPlayers.UUID, getUUID().toString()));

            coins = Integer.parseInt(values.get(COINS));
            experience = Integer.parseInt(values.get(EXPERIENCE));

            kills = Integer.parseInt(values.get(KILLS));
            deaths = Integer.parseInt(values.get(DEATHS));
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

    public class KitData {

        private final long kitId;

        private int unlockedLevel;

        private int kills;
        private int deaths;

        public KitData(long kitId) {
            this.kitId = kitId;

            this.unlockedLevel = 0;

            this.kills = 0;
            this.deaths = 0;
        }

        public void load() {
            if (!Database.get().contains(Table.KITPVP_KIT_STATS, new Column[] { TableKitPvPKitStats.UUID }, new Where(TableKitPvPKitStats.UUID, getUUID().toString()), new Where(TableKitPvPKitStats.KIT_ID, kitId))) {
                Database.get().insert(table, uuid.toString(), kitId + "", unlockedLevel + "", kills + "", deaths + "");
            } else {
                Map<Column, String> values = Database.get().getValues(table, new Column[] {
                        TableKitPvPKitStats.UNLOCKED_LEVEL,
                        TableKitPvPKitStats.KILLS,
                        TableKitPvPKitStats.DEATHS
                }, new Where(TableKitPvPKitStats.UUID, getUUID().toString()));

                unlockedLevel = Integer.parseInt(values.get(TableKitPvPKitStats.UNLOCKED_LEVEL));

                kills = Integer.parseInt(values.get(TableKitPvPKitStats.KILLS));
                deaths = Integer.parseInt(values.get(TableKitPvPKitStats.DEATHS));
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
    }
}
