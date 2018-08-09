package com.orbitmines.spigot.leaderboards.hologram;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Donation;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableDonations;
import com.orbitmines.spigot.api.handlers.leaderboard.hologram.LastEntryHologramLeaderboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.Location;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class LeaderBoardDonations extends LastEntryHologramLeaderboard {

    public LeaderBoardDonations(Location location, double yOff, ScoreboardString title, int size) {
        super(location, yOff, title, size, Table.DONATIONS, TableDonations.UUID, TableDonations.PACKAGE_ID);
    }

    public LeaderBoardDonations(Location location, double yOff, ScoreboardString[] title, int size) {
        super(location, yOff, title, size, Table.DONATIONS, TableDonations.UUID, TableDonations.PACKAGE_ID);
    }

    @Override
    public String getValue(CachedPlayer player, int count) {
        return Donation.getById(count).getTitle();
    }
}
