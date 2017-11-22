package com.orbitmines.api;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServers;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Server {

    KITPVP("KitPvP", Color.RED),
    PRISON("Prison", Color.MAROON),
    CREATIVE("Creative", Color.FUCHSIA),
    HUB("Hub", Color.TEAL),
    SURVIVAL("Survival", Color.LIME),
    SKYBLOCK("SkyBlock", Color.PURPLE),
    FOG("FoG", Color.YELLOW),
    MINIGAMES("MiniGames", Color.WHITE);

    private final String name;
    private final Color color;

    Server(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getDisplayName() {
        return color.getChatColor() + "§l" + name;
    }

    public int getPlayers() {
        return Database.get().getInt(Table.SERVERS, TableServers.PLAYERS, new Where(TableServers.SERVER, toString()));
    }

    public void setPlayers(int players) {
        Database.get().update(Table.SERVERS, new Set(TableServers.PLAYERS, players), new Where(TableServers.SERVER, toString()));
    }

    public int getMaxPlayers() {
        return Database.get().getInt(Table.SERVERS, TableServers.MAX_PLAYERS, new Where(TableServers.SERVER, toString()));
    }

    public void setMaxPlayers(int maxPlayers) {
        Database.get().update(Table.SERVERS, new Set(TableServers.MAX_PLAYERS, maxPlayers), new Where(TableServers.SERVER, toString()));
    }

    public Status getStatus() {
        return Status.valueOf(Database.get().getString(Table.SERVERS, TableServers.STATUS, new Where(TableServers.SERVER, toString())));
    }

    public void setStatus(Status status) {
        Database.get().update(Table.SERVERS, new Set(TableServers.STATUS, status.toString()), new Where(TableServers.SERVER, toString()));
    }

    public enum Status {

        ONLINE("Online", Color.LIME),
        OFFLINE("Offline", Color.RED),
        MAINTENANCE("Maintenance", Color.FUCHSIA);

        private final String name;
        private final Color color;

        Status(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }
    }
}
