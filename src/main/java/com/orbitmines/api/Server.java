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

    KITPVP("KitPvP", Color.RED, true),
    PRISON("Prison", Color.MAROON, false),
    CREATIVE("Creative", Color.FUCHSIA, false),
    HUB("Hub", Color.TEAL, true),
    SURVIVAL("Survival", Color.LIME, false),
    SKYBLOCK("SkyBlock", Color.PURPLE, false),
    FOG("FoG", Color.YELLOW, false),
    MINIGAMES("MiniGames", Color.WHITE, true);

    private final String name;
    private final Color color;
    private final boolean cleanUpPlayerData;

    Server(String name, Color color, boolean cleanUpPlayerData) {
        this.name = name;
        this.color = color;
        this.cleanUpPlayerData = cleanUpPlayerData;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public boolean cleanUpPlayerData() {
        return cleanUpPlayerData;
    }

    public String getDisplayName() {
        return color.getChatColor() + "§l" + name;
    }

    public int getPlayers() {
        return Database.get().contains(Table.SERVERS, TableServers.SERVER, new Where(TableServers.SERVER, toString())) ? Database.get().getInt(Table.SERVERS, TableServers.PLAYERS, new Where(TableServers.SERVER, toString())) : 0;
    }

    public void setPlayers(int players) {
        Database.get().update(Table.SERVERS, new Set(TableServers.PLAYERS, players), new Where(TableServers.SERVER, toString()));
    }

    public int getMaxPlayers() {
        return Database.get().contains(Table.SERVERS, TableServers.SERVER, new Where(TableServers.SERVER, toString())) ? Database.get().getInt(Table.SERVERS, TableServers.MAX_PLAYERS, new Where(TableServers.SERVER, toString())) : 0;
    }

    public void setMaxPlayers(int maxPlayers) {
        Database.get().update(Table.SERVERS, new Set(TableServers.MAX_PLAYERS, maxPlayers), new Where(TableServers.SERVER, toString()));
    }

    public Status getStatus() {
        return Database.get().contains(Table.SERVERS, TableServers.SERVER, new Where(TableServers.SERVER, toString())) ? Status.valueOf(Database.get().getString(Table.SERVERS, TableServers.STATUS, new Where(TableServers.SERVER, toString()))) : Status.OFFLINE;
    }

    public void setStatus(Status status) {
        Database.get().update(Table.SERVERS, new Set[] {
                new Set(TableServers.STATUS, status.toString()),
                new Set(TableServers.LAST_UPDATE, System.currentTimeMillis())
        }, new Where(TableServers.SERVER, toString()));
    }

    public String getIp() {
        return Database.get().contains(Table.SERVERS, TableServers.SERVER, new Where(TableServers.SERVER, toString())) ? Database.get().getString(Table.SERVERS, TableServers.IP, new Where(TableServers.SERVER, toString())) : null;
    }

    public int getPort() {
        return Database.get().contains(Table.SERVERS, TableServers.SERVER, new Where(TableServers.SERVER, toString())) ? Database.get().getInt(Table.SERVERS, TableServers.PORT, new Where(TableServers.SERVER, toString())) : -1;
    }

    public long getLastUpdate() {
        return Database.get().contains(Table.SERVERS, TableServers.SERVER, new Where(TableServers.SERVER, toString())) ? Database.get().getLong(Table.SERVERS, TableServers.LAST_UPDATE, new Where(TableServers.SERVER, toString())) : -1;
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

        public String getDisplayName() {
            return color.getChatColor() + "§l" + name;
        }
    }
}
