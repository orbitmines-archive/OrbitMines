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
public abstract class DefaultScoreboard extends ScoreboardSet {

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
            if (rank == StaffRank.NONE)
                continue;

            ScoreboardTeam team = new ScoreboardTeam(getTeamName(rank));

            team.setPrefix(rank.getPrefix(null) + "§r ");

            team.setColor(ColorUtils.toChatColor(rank.getPrefixColor()));

            teams.add(team);
            staffRankTeams.put(rank, team);
        }

        VipRank[] vipRanks = VipRank.values();
        ArrayUtils.reverse(vipRanks);

        for (VipRank rank : vipRanks) {
            ScoreboardTeam team = new ScoreboardTeam(getTeamName(rank));

            if (rank != VipRank.NONE)
                team.setPrefix(rank.getPrefix(null) + "§r ");

            team.setColor(ColorUtils.toChatColor(rank.getPrefixColor()));

            teams.add(team);
            vipRankTeams.put(rank, team);
        }
    }

    /* Order ranks in tablist */
    private String getTeamName(StaffRank staffRank) {
        switch (staffRank) {

            case OWNER:
                return "a";
            case ADMIN:
                return "b";
            case DEVELOPER:
                return "c";
            case MODERATOR:
                return "d";
            case BUILDER:
                return "e";
        }
        throw new IllegalStateException();
    }
    private String getTeamName(VipRank vipRank) {
        switch (vipRank) {

            case EMERALD:
                return "f";
            case DIAMOND:
                return "g";
            case GOLD:
                return "h";
            case IRON:
                return "i";
            case NONE:
                return "j";
        }
        throw new IllegalStateException();
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
