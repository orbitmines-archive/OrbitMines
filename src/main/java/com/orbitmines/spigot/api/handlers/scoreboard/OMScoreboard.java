package com.orbitmines.spigot.api.handlers.scoreboard;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class OMScoreboard {

    private Board board;
    private ScoreboardSet set;
    private OMPlayer omp;

    public OMScoreboard(OMPlayer omp) {
        this.board = new Board(omp.getName());
        this.omp = omp;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ScoreboardSet getSet() {
        return set;
    }

    public OMPlayer getPlayer() {
        return omp;
    }

    public void set(ScoreboardSet set) {
        if (set != null) {
            if (omp.hasScoreboard() || set.canBypassSettings()) {
                if (this.set != null) {
                    for (int score : this.set.getScores().keySet()) {
                        if (!set.getScores().containsKey(score))
                            board.remove(score, this.set.getScore(score).getString());
                    }
                }

                board.setTitle(set.getTitle().getString());

                for (int score : set.getScores().keySet()) {
                    board.add(set.getScore(score).getString(), score);
                }
            } else {
                this.board = new Board(omp.getName());
                omp.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            }

            board.update();
            board.send(omp.getPlayer());

            if (this.set != null)
                updateTeams();
        } else {
            this.board = new Board(omp.getName());
            omp.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

            for (Team team : omp.getPlayer().getScoreboard().getTeams()) {
                for (String entry : team.getEntries()) {
                    team.removeEntry(entry);
                }
            }
        }

        this.set = set;
    }

    public void clear() {
        this.board = new Board(omp.getName());
        set(set);
    }

    public void update() {
        if (set == null)
            return;

        set(set);
    }

    private void updateTeams() {
        for (ScoreboardTeam team : set.getTeams()) {
            team.update(board.getScoreboard());
        }
    }

    private Team getTeam(org.bukkit.scoreboard.Scoreboard scoreboard, String name, String prefix) {
        Team team = scoreboard.getTeam(name);
        if (team != null)
            return team;

        team = scoreboard.registerNewTeam(name);
        team.setPrefix(prefix);

        return team;
    }
}
