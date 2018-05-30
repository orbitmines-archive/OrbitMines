package com.orbitmines.spigot.api.handlers.leaderboard.hologram;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 29-7-2017
 */
public class DefaultHologramLeaderBoard extends HologramLeaderBoard {

    protected final int size;

    protected List<UUID> ordered;
    protected final HashMap<UUID, Integer> countMap;

    public DefaultHologramLeaderBoard(Location location, double yOff, ScoreboardString title, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        this(location, yOff, new ScoreboardString[] { title }, size, table, uuidColumn, column, wheres);
    }

    public DefaultHologramLeaderBoard(Location location, double yOff,  ScoreboardString[] title, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location, yOff, table, uuidColumn, column, wheres);

        this.size = size;
        this.ordered = new ArrayList<>();
        this.countMap = new HashMap<>();

        for (ScoreboardString string : title) {
            hologram.addLine(string, true);
        }

        hologram.addEmptyLine(true);

        for (int i = 0; i < size; i++) {
            int index = i;

            hologram.addLine(() -> {
                if (ordered.size() < index + 1)
                    return null; /* Empty Line */

                UUID uuid = ordered.get(index);
                CachedPlayer player = CachedPlayer.getPlayer(uuid);
                int count = countMap.get(uuid);

                StaffRank staffRank = player.getStaffRank();
                VipRank vipRank = player.getVipRank();

                return "§7" + (index + 1) + ". " + (staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor()).getChatColor() + player.getPlayerName() + "  §6" + getValue(player, count);
            }, true);
        }
    }

    @Override
    public void update() {
        /* Clear from previous update */
        this.ordered.clear();
        this.countMap.clear();

        /* Update top {size} players */
        List<Map<Column, String>> entries = Database.get().getEntries(table, columnArray, wheres);

        Map<String, Integer> map = new HashMap<>();

        for (Map<Column, String> entry : entries) {
            String uuidString = entry.get(columnArray[0]);
            int count = Integer.parseInt(entry.get(columnArray[1]));

            map.put(uuidString, count);
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

       /* Update Hologram */
       hologram.update();
    }

    public int getSize() {
        return size;
    }

    /* Override this method to change to change the message displayed at the end */
    public String getValue(CachedPlayer player, int count) {
        return count + "";
    }
}
