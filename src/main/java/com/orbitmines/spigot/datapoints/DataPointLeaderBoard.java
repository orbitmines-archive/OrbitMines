package com.orbitmines.spigot.datapoints;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class DataPointLeaderBoard extends DataPointSign {

    private Map<Location, String[]> leaderboardData;

    public DataPointLeaderBoard() {
        super("LEADERBOARD", Type.IRON_PLATE, Material.WOOL, DyeColor.RED.getWoolData());

        leaderboardData = new HashMap<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        leaderboardData.put(location.add(0.5, 0.5, 0.5), data);
        return true;
    }

    @Override
    public boolean setup() {
        if (leaderboardData.isEmpty()) {
            failureMessage = "Did not find any leaderboard data points";
            return false;
        }

        /* Add delay for LeaderBoard setup */
        new BukkitRunnable() {
            @Override
            public void run() {
                LeaderBoard.setup(leaderboardData);
            }
        }.runTaskLater(OrbitMines.getInstance(), 1);

        return true;
    }

    public Map<Location, String[]> getLeaderboardData() {
        return leaderboardData;
    }
}
