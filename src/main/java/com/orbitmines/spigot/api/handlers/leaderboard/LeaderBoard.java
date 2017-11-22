package com.orbitmines.spigot.api.handlers.leaderboard;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class LeaderBoard {

    private static List<LeaderBoard> leaderBoards = new ArrayList<>();

    protected Location location;
    protected Hologram hologram;

    public LeaderBoard(Location location) {
        leaderBoards.add(this);

        this.location = location;
        hologram = new Hologram(location);
    }

    public abstract void update();

    public Location getLocation() {
        return location;
    }

    public static List<LeaderBoard> getLeaderBoards() {
        return leaderBoards;
    }

    public static void setup(Map<Location, String[]> leaderboardData) {
        Server server = OrbitMines.getInstance().getServerHandler().getServer();

        Map<String, Instantiator> instantiators = Instantiator.getInstantiators();

        for (Location location : leaderboardData.keySet()) {
            String[] data = leaderboardData.get(location);

            String name = data[0].toUpperCase();

            if (instantiators.containsKey(name))
                instantiators.get(name).instantiate(server, location, data);
        }
    }

    public static abstract class Instantiator {

        private static Map<String, Instantiator> instantiators = new HashMap<>();

        protected final String datapointName;

        public Instantiator(String datapointName) {
            this.datapointName = datapointName;

            instantiators.put(datapointName, this);
        }

        public abstract LeaderBoard instantiate(Server server, Location location, String[] data);

        public String getDatapointName() {
            return datapointName;
        }

        public static Map<String, Instantiator> getInstantiators() {
            return instantiators;
        }
    }
}
