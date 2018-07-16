package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.handlers.data.SettingsData;
import com.orbitmines.spigot.servers.hub.gui.stats.StatsGUI;

import java.util.List;
import java.util.UUID;

public class CommandStats extends Command {

    private String[] alias = { "/stats", "/statistics", "/stat" };

    public CommandStats() {
        super(null);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "(player)";
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (a.length == 1) {
            new StatsGUI(omp).open(omp);
            return;
        }

        OMPlayer omp2 = OMPlayer.getPlayer(a[1]);

        if (omp2 != null) {
            SettingsType type = ((SettingsData) omp2.getData(Data.Type.SETTINGS)).getSettings().get(Settings.PRIVATE_MESSAGES);

            if (type != SettingsType.ENABLED && !omp.isEligible(StaffRank.MODERATOR)) {
                switch (type) {
                    case DISABLED: {
                        omp.sendMessage("Stats", Color.RED, omp2.getName() + " §7heeft stats uit staan.", omp2.getName() + " §7has stats disabled.");
                        return;
                    }
                    case ONLY_FRIENDS: {
                        List<UUID> friends = ((FriendsData) omp2.getData(Data.Type.FRIENDS)).getFriends(true);

                        if (!friends.contains(omp.getUUID())) {
                            omp.sendMessage("Stats", Color.RED, omp2.getName() + " §7heeft stats op alleen §bVrienden§7 staan.", omp2.getName() + " §7has stats set to §bFriends§7 only.");
                            return;
                        }
                        break;
                    }
                    case ONLY_FAVORITE_FRIENDS: {
                        List<UUID> favorites = ((FriendsData) omp2.getData(Data.Type.FRIENDS)).getFavoriteFriends();

                        if (!favorites.contains(omp.getUUID())) {
                            omp.sendMessage("Stats", Color.RED, omp2.getName() + " §7heeft stats op alleen §6Favoriete Vrienden§7 staan.", omp2.getName() + " §7has stats set to §6Favorite Friends§7 only.");
                            return;
                        }
                        break;
                    }
                }
            }

            new StatsGUI(omp2).open(omp);
            return;
        }

        omp.sendMessage("Stats", Color.RED, "Speler '" + a[1] + "' is niet online!", "Player '" + a[1] + "' is not online!");
    }
}
