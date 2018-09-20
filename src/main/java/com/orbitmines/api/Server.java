package com.orbitmines.api;

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.TablePlayTime;
import com.orbitmines.api.database.tables.TableServers;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.servers.creative.handlers.CreativeAchievements;
import com.orbitmines.spigot.servers.fog.handlers.FoGAchievements;
import com.orbitmines.spigot.servers.hub.handlers.HubAchievements;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPAchievements;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamesAchievements;
import com.orbitmines.spigot.servers.prison.handlers.PrisonAchievements;
import com.orbitmines.spigot.servers.skyblock.handlers.SkyBlockAchievements;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalAchievements;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHSurvivalAchievements;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Server {

    HUB("Hub", Color.TEAL, true, HubAchievements.class, TablePlayTime.HUB),
    SURVIVAL("Survival", Color.LIME, false, SurvivalAchievements.class, TablePlayTime.SURVIVAL),
    KITPVP("KitPvP", Color.RED, true, KitPvPAchievements.class, TablePlayTime.KITPVP),

    PRISON("Prison", Color.MAROON, false, PrisonAchievements.class, TablePlayTime.PRISON),
    CREATIVE("Creative", Color.FUCHSIA, false, CreativeAchievements.class, TablePlayTime.CREATIVE),
    SKYBLOCK("SkyBlock", Color.PURPLE, false, SkyBlockAchievements.class, TablePlayTime.SKYBLOCK),
    FOG("FoG", Color.YELLOW, false, FoGAchievements.class, TablePlayTime.FOG),
    MINIGAMES("MiniGames", Color.WHITE, true, MiniGamesAchievements.class, TablePlayTime.MINIGAMES),
    UHSURVIVAL("UHSurvival", Color.GREEN, false, UHSurvivalAchievements.class, TablePlayTime.UHSURVIVAL);

    private final String name;
    private final Color color;
    private final boolean cleanUpPlayerData;
    private final Class<? extends Enum> achievementClass;
    private final Column playTimeColumn;

    Server(String name, Color color, boolean cleanUpPlayerData, Class<? extends Enum> achievementClass, Column playTimeColumn) {
        this.name = name;
        this.color = color;
        this.cleanUpPlayerData = cleanUpPlayerData;
        this.achievementClass = achievementClass;
        this.playTimeColumn = playTimeColumn;
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

    public Column getPlayTimeColumn() {
        return playTimeColumn;
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

    public Achievement achievementValueOf(String value) {
        return (Achievement) Enum.valueOf(achievementClass, value);
    }

    public Achievement[] achievementValues() {
        return (Achievement[]) achievementClass.getEnumConstants();
    }

    public enum Status {

        ONLINE("Online", Color.LIME),
        OFFLINE("Offline", Color.RED),
        MAINTENANCE("Maintenance", Color.FUCHSIA),
        RESTARTING("Restarting", Color.GRAY);

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
