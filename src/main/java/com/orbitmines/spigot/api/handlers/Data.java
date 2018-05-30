package com.orbitmines.spigot.api.handlers;

import com.orbitmines.api.database.Table;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class Data {

    protected final Table table;
    protected final Type type;
    protected final UUID uuid;

    public Data(Table table, Type type, UUID uuid) {
        this.table = table;
        this.type = type;
        this.uuid = uuid;
    }

    public abstract void load();

    public UUID getUUID() {
        return uuid;
    }

    public enum Type {

        VOTES,
        FRIENDS,
        SETTINGS;

    }
}
