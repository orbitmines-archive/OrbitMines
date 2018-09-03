package com.orbitmines.spigot.api.handlers.leaderboard.cmd;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import org.bukkit.Location;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 29-7-2017
 */
public abstract class DefaultCommandLeaderBoard extends LeaderBoard {

    protected final String name;
    protected final Color color;
    protected final int size;

    protected final Command command;

    protected List<Map<Column, String>> ordered;

    protected int totalCount;

    public DefaultCommandLeaderBoard(String name, Color color, Server server, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(null, table, uuidColumn, column, wheres);

        this.name = name;
        this.color = color;
        this.size = size;
        this.ordered = new ArrayList<>();

        command = new Command(server) {
            @Override
            public String[] getAlias() {
                return DefaultCommandLeaderBoard.this.getAlias();
            }

            @Override
            public String getHelp(OMPlayer omp) {
                return DefaultCommandLeaderBoard.this.getHelp();
            }

            @Override
            public void dispatch(OMPlayer omp, String[] a) {
                omp.sendMessage("");
                omp.sendMessage(" §8§lOrbit§7§lMines " + color.getChatColor() + "§l" + name);

                for (int i = 0; i < size; i++) {
                    if (ordered.size() < i + 1)
                        continue;

                    Map<Column, String> entry = ordered.get(i);

                    UUID uuid = UUID.fromString(entry.get(columnArray[0]));
                    CachedPlayer player = CachedPlayer.getPlayer(uuid);
                    int count = Integer.parseInt(entry.get(columnArray[1]));

                    StaffRank staffRank = player.getStaffRank();
                    VipRank vipRank = player.getVipRank();

                    String color;
                    switch (i) {
                        case 0:
                            color = "§6";
                            break;
                        case 1:
                            color = "§7";
                            break;
                        case 2:
                            color = "§c";
                            break;
                        default:
                            color = "§8";
                            break;
                    }

                    omp.sendMessage("  " + color + "§l" + (i + 1) + ". " + (staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor()).getChatColor() + player.getPlayerName() + " §8- " + getValue(player, count));
                }

                DefaultCommandLeaderBoard.this.onDispatch(omp, a);
            }
        };
    }

    public abstract String[] getAlias();

    public abstract void onDispatch(OMPlayer omp, String[] a);

    public abstract String getHelp();

    @Override
    public void update() {
        /* Clear from previous update */
        this.ordered.clear();
        this.totalCount = 0;

        /* Update top {size} players */
        List<Map<Column, String>> entries = Database.get().getEntries(table, columnArray, wheres);

        /* Update Total Count */
        for (Map<Column, String> entry : entries) {
            totalCount += Integer.parseInt(entry.get(columnArray[1]));
        }

        this.ordered = getOnLeaderBoard(entries);
    }

    @Override
    public Location getLocation() {
        throw new IllegalStateException();
    }

    public int getSize() {
        return size;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /* Override this method to change the displayed uuids */
    protected List<Map<Column, String>> getOnLeaderBoard(List<Map<Column, String>> entries) {
        List<Map<Column, String>> ordered = new ArrayList<>(entries);
        ordered.sort((m1, m2) -> Integer.parseInt(m2.get(columnArray[1])) - Integer.parseInt(m1.get(columnArray[1])));

        if (ordered.size() > size)
            ordered = ordered.subList(0, size);

        return ordered;
    }

    /* Override this method to change to change the message displayed at the end */
    public String getValue(CachedPlayer player, int count) {
        return "§6" + count + "";
    }
}
