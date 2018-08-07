package com.orbitmines.spigot.api.handlers.scoreboard;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.ColorUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class DefaultScoreboard extends ScoreboardSet {

    private List<ScoreboardTeam> teams;
    private Map<StaffRank, ScoreboardTeam> staffRankTeams;
    private Map<VipRank, ScoreboardTeam> vipRankTeams;

    public DefaultScoreboard(OMPlayer omp, ScoreboardString title, ScoreboardString... scores) {
        super(omp, title, scores);

        teams = new ArrayList<>();
        staffRankTeams = new HashMap<>();
        vipRankTeams = new HashMap<>();

        StaffRank[] staffRanks = StaffRank.values();
        ArrayUtils.reverse(staffRanks);

        for (StaffRank rank : staffRanks) {
            ScoreboardTeam team = new ScoreboardTeam(rank.toString());

            if (rank != StaffRank.NONE && rank != StaffRank.ADMIN)
                team.setPrefix(rank.getPrefix(null) + "§r ");

            team.setColor(ColorUtils.toChatColor(rank.getPrefixColor()));

            teams.add(team);
            staffRankTeams.put(rank, team);
        }

        VipRank[] vipRanks = VipRank.values();
        ArrayUtils.reverse(vipRanks);

        for (VipRank rank : vipRanks) {
            ScoreboardTeam team = new ScoreboardTeam(rank.toString());

            if (rank != VipRank.NONE)
                team.setPrefix(rank.getPrefix(null) + "§r ");

            team.setColor(ColorUtils.toChatColor(rank.getPrefixColor()));

            teams.add(team);
            vipRankTeams.put(rank, team);
        }
    }

    @Override
    public List<ScoreboardTeam> getTeams() {
        update();
        return teams;
    }

    private void update() {
        for (ScoreboardTeam team : teams) {
            team.getPlayers().clear();
        }

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            ScoreboardTeam team;
            switch (omp.getStaffRank()) {

                case NONE:
                    team = vipRankTeams.get(omp.getVipRank());
                    break;
                default:
                    team = staffRankTeams.get(omp.getStaffRank());
                    break;
            }

            team.getPlayers().add(omp.getPlayer());
        }
    }
}
