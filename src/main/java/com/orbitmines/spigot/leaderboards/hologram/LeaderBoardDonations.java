package com.orbitmines.spigot.leaderboards.hologram;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Donation;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableDonations;
import com.orbitmines.spigot.api.handlers.leaderboard.hologram.DefaultHologramLeaderBoard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.Location;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class LeaderBoardDonations extends DefaultHologramLeaderBoard {

    public LeaderBoardDonations(Location location, double yOff, ScoreboardString title, int size) {
        super(location, yOff, title, size, Table.DONATIONS, TableDonations.UUID, TableDonations.PACKAGE_ID);
    }

    public LeaderBoardDonations(Location location, double yOff, ScoreboardString[] title, int size) {
        super(location, yOff, title, size, Table.DONATIONS, TableDonations.UUID, TableDonations.PACKAGE_ID);
    }

    @Override
    public String getValue(CachedPlayer player, int count) {
        String donation;
        try {
            donation = Donation.getById(count).getTitle();
        } catch(IllegalArgumentException ex) {
            ConsoleUtils.warn("Invalid Donation for " + player.getUUID().toString() + " with Package ID #" + count + ".");
            donation = "§c§lUNKNOWN";
        }

        return donation;
    }
}
