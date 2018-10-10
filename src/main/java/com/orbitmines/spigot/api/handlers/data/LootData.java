package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.Rarity;
import com.orbitmines.api.database.*;
import com.orbitmines.api.utils.LootUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.Data;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.TableLoot.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class LootData extends Data {

    private DiscordBot discord;
    private BotToken token;

    private List<Loot.Instance> loot;

    public LootData(UUID uuid) {
        super(Table.LOOT, Type.LOOT, uuid);

        this.loot = new ArrayList<>();

        OrbitMinesServer server = OrbitMines.getInstance().getServerHandler();
        this.discord = server.getDiscord();
        this.token = server.getToken();
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

        LootUtils.Action action;

        if (instance == null) {
            Database.get().insert(table, getUUID().toString(), loot.toString(), rarity.toString(), count + "", description.replaceAll("§", "&"));
            this.loot.add(new Loot.Instance(loot, rarity, count, description));

            action = LootUtils.Action.INSERT;
        } else {
            Database.get().update(table, new Set(COUNT, count), new Where(UUID, getUUID().toString()), new Where(LOOT, loot.toString()), new Where(RARITY, rarity.toString()), new Where(DESCRIPTION, description.replaceAll("§", "&")));
            instance.setCount(count);

            action = LootUtils.Action.UPDATE;
        }

        LootUtils.log(discord, token, this.uuid, loot.toString(), loot.getServer(count), rarity, description, count, action);
    }

    public void remove(Loot loot, Rarity rarity, String description) {
        Loot.Instance instance = getInstance(loot, rarity, description);
        this.loot.remove(instance);

        Database.get().delete(table, new Where(UUID, getUUID().toString()), new Where(LOOT, loot.toString()), new Where(RARITY, rarity.toString()), new Where(DESCRIPTION, description.replaceAll("§", "&")));

        LootUtils.log(discord, token, this.uuid, loot.toString(), loot.getServer(instance.getCount()), rarity, description, instance.getCount(), LootUtils.Action.DELETE);
    }

    private Loot.Instance getInstance(Loot loot, Rarity rarity, String description) {
        for (Loot.Instance instance : this.loot) {
            if (instance.getLoot() == loot && instance.getRarity() == rarity && instance.getDescription().equals(description))
                return instance;
        }
        return null;
    }
}
