package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.Rarity;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.Data;
import org.bukkit.ChatColor;

import java.util.*;

import static com.orbitmines.api.database.tables.TableLoot.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class LootData extends Data {

    private List<Loot.Instance> loot;

    public LootData(UUID uuid) {
        super(Table.LOOT, Type.LOOT, uuid);

        this.loot = new ArrayList<>();
    }

    @Override
    public void load() {
        loot.clear();

        List<Map<Column, String>> entries = Database.get().getEntries(table, new Where(UUID, getUUID().toString()));

        for (Map<Column, String> entry : entries) {
            Loot loot = Loot.valueOf(entry.get(LOOT));
            Rarity rarity = Rarity.valueOf(entry.get(RARITY));
            int count = Integer.parseInt(entry.get(COUNT));
            String description = ChatColor.translateAlternateColorCodes('&', entry.get(DESCRIPTION));

            this.loot.add(new Loot.Instance(loot, rarity, count, description));
        }
    }

    public List<Loot.Instance> getLoot() {
        return loot;
    }

    public int getCount(Loot loot, Rarity rarity, String description) {
        Loot.Instance instance = getInstance(loot, rarity, description);
        return instance == null ? 0 : instance.getCount();
    }

    public void add(Loot loot, Rarity rarity, String description, int count) {
        set(loot, rarity, description, getCount(loot, rarity, description) + count);
    }

    public void set(Loot loot, Rarity rarity, String description, int count) {
        if (count == 0) {
            remove(loot, rarity, description);
            return;
        }

        Loot.Instance instance = getInstance(loot, rarity, description);

        if (instance == null) {
            Database.get().insert(table, getUUID().toString(), loot.toString(), rarity.toString(), count + "", description.replaceAll("§", "&"));
            this.loot.add(new Loot.Instance(loot, rarity, count, description));
        } else {
            Database.get().update(table, new Set(COUNT, count), new Where(UUID, getUUID().toString()), new Where(LOOT, loot.toString()), new Where(RARITY, rarity.toString()), new Where(DESCRIPTION, description.replaceAll("§", "&")));
            instance.setCount(count);
        }
    }

    public void remove(Loot loot, Rarity rarity, String description) {
        this.loot.remove(getInstance(loot, rarity, description));

        Database.get().delete(table, new Where(UUID, getUUID().toString()), new Where(LOOT, loot.toString()), new Where(RARITY, rarity.toString()), new Where(DESCRIPTION, description.replaceAll("§", "&")));
    }

    private Loot.Instance getInstance(Loot loot, Rarity rarity, String description) {
        for (Loot.Instance instance : this.loot) {
            if (instance.getLoot() == loot && instance.getRarity() == rarity && instance.getDescription().equals(description))
                return instance;
        }
        return null;
    }
}
