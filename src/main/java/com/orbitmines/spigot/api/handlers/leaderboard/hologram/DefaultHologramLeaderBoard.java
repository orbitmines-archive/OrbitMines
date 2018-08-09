package com.orbitmines.spigot.api.handlers.leaderboard.hologram;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 29-7-2017
 */
public class DefaultHologramLeaderBoard extends HologramLeaderBoard {

    protected final int size;

    protected List<Map<Column, String>> ordered;

    public DefaultHologramLeaderBoard(Location location, double yOff, ScoreboardString title, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        this(location, yOff, new ScoreboardString[] { title }, size, table, uuidColumn, column, wheres);
    }

    public DefaultHologramLeaderBoard(Location location, double yOff,  ScoreboardString[] title, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location, yOff, table, uuidColumn, column, wheres);

        this.size = size;
        this.ordered = new ArrayList<>();

        for (ScoreboardString string : title) {
            hologram.addLine(string, true);
        }

        hologram.addEmptyLine(true);

        for (int i = 0; i < size; i++) {
            int index = i;

            hologram.addLine(() -> {
                if (ordered.size() < index + 1)
                    return null; /* Empty Line */

                Map<Column, String> entry = ordered.get(index);

                UUID uuid = UUID.fromString(entry.get(columnArray[0]));
                CachedPlayer player = CachedPlayer.getPlayer(uuid);

                int count = Integer.parseInt(entry.get(columnArray[1]));

                if (player == null) {
                    String name = UUIDUtils.getName(uuid);

                    if (name != null)
                        return "§7" + (index + 1) + ". " + VipRank.NONE.getPrefixColor().getChatColor() + name + "  " + getValue(null, count);
                    else
                        return "§7" + (index + 1) + ". " + VipRank.NONE.getPrefixColor().getChatColor() + "UNKNOWN PLAYER" + "  " + getValue(null, count);
                }

                StaffRank staffRank = player.getStaffRank();
                VipRank vipRank = player.getVipRank();

                return "§7" + (index + 1) + ". " + ((staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) ? staffRank.getPrefixColor() : vipRank.getPrefixColor()).getChatColor() + player.getPlayerName() + "  " + getValue(player, count);
            }, true);
        }
    }

    @Override
    public void update() {
        /* Clear from previous update */
        this.ordered.clear();

        /* Update top {size} players */
        List<Map<Column, String>> entries = Database.get().getEntries(table, columnArray, wheres);

        this.ordered = getOnLeaderBoard(entries);

        /* Update Hologram */
        hologram.update();
    }

    public int getSize() {
        return size;
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
