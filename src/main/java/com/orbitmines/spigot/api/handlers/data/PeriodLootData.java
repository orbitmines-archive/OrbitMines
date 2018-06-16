package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.database.*;
import com.orbitmines.spigot.api.PeriodLoot;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.TableLoot.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PeriodLootData extends Data {

    private Map<PeriodLoot, Long> loot;

    public PeriodLootData(UUID uuid) {
        super(Table.PERIOD_LOOT, Type.PERIOD_LOOT, uuid);

        this.loot = new HashMap<>();

        for (PeriodLoot loot : PeriodLoot.values()) {
            this.loot.put(loot, 0L);
        }
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, UUID, new Where(UUID, getUUID().toString()))) {
            /* So we don't have to add this manually; */
            ArrayList<String> list = new ArrayList<>();
            list.add(uuid.toString());
            for (int i = 0; i < PeriodLoot.values().length; i++) {
                list.add("0");
            }

            Database.get().insert(table, list.toArray(new String[list.size()]));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Where(UUID, uuid.toString()));

            for (PeriodLoot loot : PeriodLoot.values()) {
                this.loot.put(loot, Long.parseLong(values.get(loot.getColumn())));
            }
        }
    }

    public Map<PeriodLoot, Long> getLoot() {
        return loot;
    }

    public void collect(OMPlayer omp, PeriodLoot loot) {
        this.loot.put(loot, System.currentTimeMillis() / 1000L);

        Database.get().update(table, new Set(loot.getColumn(), this.loot.get(loot)), new Where(UUID, uuid.toString()));

        loot.receive(omp);
    }

    public boolean canCollect(PeriodLoot loot) {
        return (System.currentTimeMillis() / 1000L) - this.loot.get(loot) >= loot.getDelay();
    }

    public long getCooldown(PeriodLoot loot) {
        return loot.getDelay() - ((System.currentTimeMillis() / 1000L) - this.loot.get(loot));
    }
}
