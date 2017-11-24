package com.orbitmines.bungeecord.handlers;

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.TableServerData;
import net.md_5.bungee.api.ChatColor;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class MotdHandler {

    private String[] motd;

    public MotdHandler() {
        if (Database.get().contains(Table.SERVER_DATA, new Column[]{ TableServerData.SERVER, TableServerData.TYPE }, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "MOTD"))) {
            fromDatabase();
        } else {
            motd = new String[]{"§8§lOrbit§7§lMines", "§7Subtitle"};
            Database.get().insert(Table.SERVER_DATA, TableServerData.SERVER_DATA.values("BUNGEE", "MOTD", serialize()));
        }
    }

    public String getMessage() {
        return motd[0] + "\n" + motd[1];
    }

    public void setFirstLine(String firstLine) {
        motd[0] = firstLine;

        update();
    }

    public String getFirstLine() {
        return motd[0];
    }

    public void setSecondLine(String secondLine) {
        motd[1] = secondLine;

        update();
    }

    public String getSecondLine() {
        return motd[1];
    }

    private String serialize() {
        return (motd[0] + "|" + motd[1]).replaceAll("§", "&");
    }

    private void update() {
        Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, serialize()), new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "MOTD"));
    }

    private void fromDatabase() {
        String data = Database.get().getString(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "MOTD"));
        String[] motd = ChatColor.translateAlternateColorCodes('&', data).split("\\|");

        if (motd.length == 2) {
            this.motd = motd;
        } else {
            StringBuilder firstLine = new StringBuilder();
            for (int i = 0; i < motd.length - 1; i++) {
                firstLine.append(motd[i]);
            }
            this.motd = new String[]{firstLine.toString(), motd[motd.length - 1]};
        }
    }
}
