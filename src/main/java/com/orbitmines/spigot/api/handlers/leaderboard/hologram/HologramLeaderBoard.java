package com.orbitmines.spigot.api.handlers.leaderboard.hologram;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import org.bukkit.Location;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public abstract class HologramLeaderBoard extends LeaderBoard {

    protected Hologram hologram;

    public HologramLeaderBoard(Location location, double yOff, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location, table, uuidColumn, column, wheres);

        hologram = new Hologram(location, yOff, Hologram.Face.UP);
    }
}
