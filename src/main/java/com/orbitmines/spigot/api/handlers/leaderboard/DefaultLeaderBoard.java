package com.orbitmines.spigot.api.handlers.leaderboard;

import com.madblock.api.Rank;
import com.madblock.api.VipRank;
import com.madblock.api.database.Column;
import com.madblock.api.database.Database;
import com.madblock.api.database.Table;
import com.madblock.api.database.Where;
import com.madblock.api.handlers.CachedPlayer;
import org.bukkit.Location;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class DefaultLeaderBoard extends LeaderBoard {

    private final String name;

    private final Table table;
    private final Column[] columnArray;
    private final Where[] wheres;

    public DefaultLeaderBoard(Location location, String name, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location);

        this.name = name;
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

        if (ordered.size() > 10)
            ordered = ordered.subList(ordered.size() -10, ordered.size());

        hologram.clearLines();
        hologram.addLine(name);
        hologram.addLine("");

        for (int i = 0; i < ordered.size(); i++) {
            String stringUuid = ordered.get(ordered.size() -1 -i);

            CachedPlayer player = CachedPlayer.getPlayer(UUID.fromString(stringUuid));
            int count = map.get(stringUuid);

            Rank rank = player.getRank();
            VipRank vipRank = player.getVipRank();

            hologram.addLine("§7" + (i + 1) + ". " + (rank != Rank.NONE ? rank.getPrefixColor() : vipRank.getPrefixColor()).getChatColor() + player.getPlayerName() + "  §6" + getValue(player, count));
        }

        hologram.create();
        hologram.hideLine(2);
    }

    public String getValue(CachedPlayer player, int count) {
        return count + "";
    }
}
