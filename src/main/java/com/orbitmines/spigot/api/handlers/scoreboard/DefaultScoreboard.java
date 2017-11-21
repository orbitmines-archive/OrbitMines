package com.orbitmines.spigot.api.handlers.scoreboard;

import com.madblock.api.Color;
import com.madblock.api.Rank;
import com.madblock.api.VipRank;
import com.madblock.spigot.api.handlers.OMPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* MadBlock, LLC CONFIDENTIAL - @author Fadi Shawki - 2017
* __________________
*
*  2017 MadBlock, LLC 
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of MadBlock, LLC and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to MadBlock, LLC
* and its suppliers and may be covered by U.S. and Foreign Patents,
* patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from MadBlock, LLC.
*/
public class DefaultScoreboard extends ScoreboardSet {

    private List<ScoreboardTeam> teams;
    private Map<Rank, ScoreboardTeam> staffRankTeams;
    private Map<VipRank, ScoreboardTeam> vipRankTeams;

    public DefaultScoreboard(OMPlayer omp, ScoreboardString title, ScoreboardString... scores) {
        super(mbp, title, scores);

        teams = new ArrayList<>();
        staffRankTeams = new HashMap<>();
        vipRankTeams = new HashMap<>();

        for (Rank rank : Rank.values()) {
            ScoreboardTeam team = new ScoreboardTeam(rank.toString());
            team.setPrefix(rank.getPrefix(Color.WHITE));

            teams.add(team);
            staffRankTeams.put(rank, team);
        }

        for (VipRank rank : VipRank.values()) {
            ScoreboardTeam team = new ScoreboardTeam(rank.toString());
            team.setPrefix(rank.getPrefix(Color.WHITE));

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
            switch (mbp.getRank()) {

                case NONE:
                    team = vipRankTeams.get(mbp.getVipRank());
                    break;
                default:
                    team = staffRankTeams.get(mbp.getRank());
                    break;
            }

            team.getPlayers().add(mbp.getPlayer());
        }
    }
}
