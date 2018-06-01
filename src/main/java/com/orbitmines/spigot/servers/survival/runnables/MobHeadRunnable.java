package com.orbitmines.spigot.servers.survival.runnables;
/*
 * OrbitMines - @author Fadi Shawki - 1-6-2018
 */

import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.survival.Survival;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MobHeadRunnable extends SpigotRunnable {

    private Survival survival;

    public MobHeadRunnable(Survival survival) {
        super(TimeUnit.HOUR, 1);

        this.survival = survival;
    }

    @Override
    public void run() {
        List<Map<Column, String>> entries = Database.get().getEntries(Table.PLAYERS, new Column[] { TablePlayers.UUID, TablePlayers.VIPRANK });

        Map<VipRank, List<UUID>> mobHeads = survival.getMobHeads();
        for (VipRank vipRank : VipRank.values()) {
            mobHeads.get(vipRank).clear();
        }

        for (Map<Column, String> entry : entries) {
            VipRank vipRank = VipRank.valueOf(entry.get(TablePlayers.VIPRANK));
            if (vipRank == VipRank.NONE)
                continue;

            mobHeads.get(vipRank).add(UUID.fromString(entry.get(TablePlayers.UUID)));
        }
    }
}
