package com.orbitmines.spigot.api.handlers.leaderboard;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import org.bukkit.Location;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class DefaultLeaderBoard extends LeaderBoard {

    private final String name;
    private final int size;

    private final Table table;
    private final Column[] columnArray;
    private final Where[] wheres;

    public DefaultLeaderBoard(Location location, String name, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location);

        this.name = name;
        this.size = size;
        this.table = table;
        this.columnArray = new Column[] { uuidColumn, column };
        this.wheres = wheres;
    }

    @Override
    public void update() {
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

        hologram.clearLines();
        hologram.addLine(name);
        hologram.addLine("");

        for (int i = 0; i < ordered.size(); i++) {
            String stringUuid = ordered.get(ordered.size() -1 -i);

            CachedPlayer player = CachedPlayer.getPlayer(UUID.fromString(stringUuid));
            int count = map.get(stringUuid);

            StaffRank staffRank = player.getStaffRank();
            VipRank vipRank = player.getVipRank();

            hologram.addLine("§7" + (i + 1) + ". " + (staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor()).getChatColor() + player.getPlayerName() + "  §6" + getValue(player, count));
        }

        hologram.create();
        hologram.hideLine(2);
    }

    public String getValue(CachedPlayer player, int count) {
        return count + "";
    }
}
