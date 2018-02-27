package com.orbitmines.spigot.api.handlers.leaderboard.custom;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Donation;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableDonations;
import com.orbitmines.spigot.api.handlers.leaderboard.DefaultLeaderBoard;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.Location;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class LeaderBoardDonations extends DefaultLeaderBoard {

    public LeaderBoardDonations(Location location, String name, int size) {
        super(location, name, size, Table.DONATIONS, TableDonations.UUID, TableDonations.PACKAGE_ID);
    }

    @Override
    public void update() {
        List<Map<Column, String>> entries = Database.get().getEntries(table, columnArray, wheres);

        if (entries.size() > size)
            entries = entries.subList(entries.size() -size, entries.size());

        hologram.clearLines();
        hologram.addLine(name);
        hologram.addLine("");

        for (int i = 0; i < entries.size(); i++) {
            Map<Column, String> entry = entries.get(entries.size() -1 -i);
            String stringUuid = entry.get(columnArray[0]);
            int packageId = Integer.parseInt(entry.get(columnArray[1]));

            Donation donation;
            try {
                donation = Donation.getById(packageId);
            } catch(IllegalArgumentException ex) {
                ConsoleUtils.warn("Invalid Donation for " + stringUuid + " with Package ID #" + packageId + ".");
                continue;
            }

            CachedPlayer player = CachedPlayer.getPlayer(UUID.fromString(stringUuid));

            StaffRank staffRank = player.getStaffRank();
            VipRank vipRank = player.getVipRank();

            hologram.addLine("§7" + (i + 1) + ". " + (staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor()).getChatColor() + player.getPlayerName() + "  §6" + donation.getTitle());
        }

        hologram.create();
        hologram.hideLine(2);
    }

    @Override
    public String getValue(CachedPlayer player, int count) {
        throw new IllegalStateException();
    }
}
