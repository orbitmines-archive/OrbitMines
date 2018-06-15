package com.orbitmines.spigot.api.handlers.leaderboard.cmd;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.spigot.api.handlers.CachedPlayer;
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

    protected List<UUID> ordered;
    protected final HashMap<UUID, Integer> countMap;

    protected int totalCount;

    public DefaultCommandLeaderBoard(String name, Color color, Server server, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(null, table, uuidColumn, column, wheres);

        this.name = name;
        this.color = color;
        this.size = size;
        this.ordered = new ArrayList<>();
        this.countMap = new HashMap<>();

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
                omp.sendMessage(" §7§lOrbit§8§lMines " + color.getChatColor() + "§l" + name);

                for (int i = 0; i < size; i++) {
                    if (ordered.size() < i + 1)
                        continue;

                    UUID uuid = ordered.get(i);
                    CachedPlayer player = CachedPlayer.getPlayer(uuid);
                    int count = countMap.get(uuid);

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
        this.countMap.clear();
        this.totalCount = 0;

        /* Update top {size} players */
        List<Map<Column, String>> entries = Database.get().getEntries(table, columnArray, wheres);

        Map<String, Integer> map = new HashMap<>();

        for (Map<Column, String> entry : entries) {
            String uuidString = entry.get(columnArray[0]);
            int count = Integer.parseInt(entry.get(columnArray[1]));

            map.put(uuidString, count);
            totalCount += count;
        }

        List<String> ordered = new ArrayList<>(map.keySet());
        ordered.sort(Comparator.comparing(map::get));

        if (ordered.size() > size)
            ordered = ordered.subList(ordered.size() -size, ordered.size());

        for (int i = 0; i < ordered.size(); i++) {
            String stringUUID = ordered.get(ordered.size() -1 -i);
            UUID uuid = UUID.fromString(stringUUID);

            this.ordered.add(uuid);
            this.countMap.put(uuid, map.get(stringUUID));
        }
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

    /* Override this method to change to change the message displayed at the end */
    public String getValue(CachedPlayer player, int count) {
        return "§6" + count + "";
    }
}
