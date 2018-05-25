package com.orbitmines.spigot.api.handlers.leaderboard;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public abstract class LeaderBoard {

    private static List<LeaderBoard> leaderBoards = new ArrayList<>();

    protected Location location;

    protected final Table table;
    protected final Column[] columnArray;
    protected final Where[] wheres;

    public LeaderBoard(Location location) {
        this(location, null, null, null);
    }

    public LeaderBoard(Location location, Table table, Column uuidColumn, Column column, Where... wheres) {
        leaderBoards.add(this);

        this.location = location;
        this.table = table;
        this.columnArray = new Column[] { uuidColumn, column };
        this.wheres = wheres;
    }

    public abstract void update();

    public Location getLocation() {
        return location;
    }

    public static List<LeaderBoard> getLeaderBoards() {
        return leaderBoards;
    }

    public static void setup(Map<Location, String[]> leaderboardData) {
        Map<String, Instantiator> instantiators = Instantiator.getInstantiators();

        for (Location location : leaderboardData.keySet()) {
            String[] data = leaderboardData.get(location);

            String name = data[0].toUpperCase();

            if (instantiators.containsKey(name))
                instantiators.get(name).instantiate(location, data);
        }
    }

    public static abstract class Instantiator {

        private static Map<String, Instantiator> instantiators = new HashMap<>();

        protected final String datapointName;

        public Instantiator(String datapointName) {
            this.datapointName = datapointName;

            instantiators.put(datapointName, this);
        }

        public abstract LeaderBoard instantiate(Location location, String[] data);

        public String getDatapointName() {
            return datapointName;
        }

        public static Map<String, Instantiator> getInstantiators() {
            return instantiators;
        }
    }
}
