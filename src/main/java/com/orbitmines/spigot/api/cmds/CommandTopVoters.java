package com.orbitmines.spigot.api.cmds;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.api.handlers.CachedPlayer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.leaderboard.cmd.DefaultCommandLeaderBoard;

public class CommandTopVoters extends DefaultCommandLeaderBoard {

    private String[] alias = { "/topvoters", "/topvoters", "/voters" };

    public CommandTopVoters() {
        super("Top Voters of " + DateUtils.getMonth() + " " + DateUtils.getYear(), Color.BLUE, null, 5, Table.VOTES, TableVotes.UUID, TableVotes.VOTES);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        omp.sendMessage(" §9§lRewards");

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getValue(CachedPlayer player, int count) {
        return "§9" + count + " " + (count == 1 ? "Vote" : "Votes");
    }
}
