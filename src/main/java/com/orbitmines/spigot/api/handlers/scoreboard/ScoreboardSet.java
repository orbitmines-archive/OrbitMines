package com.orbitmines.spigot.api.handlers.scoreboard;

import com.orbitmines.spigot.api.handlers.OMPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ScoreboardSet {

    protected OMPlayer omp;
    private final ScoreboardString title;
    private final Map<Integer, ScoreboardString> scores;

    public ScoreboardSet(OMPlayer omp, ScoreboardString title, ScoreboardString... scores) {
        this.omp = omp;
        this.title = title;
        this.scores = new HashMap<>();

        int score = scores.length -1;
        for (int i = 0; i < scores.length; i++) {
            this.scores.put(score, scores[i]);
            score--;
        }
    }

    public abstract List<ScoreboardTeam> getTeams();

    public abstract boolean canBypassSettings();

    public OMPlayer getPlayer() {
        return omp;
    }

    public ScoreboardString getTitle() {
        return title;
    }

    public Map<Integer, ScoreboardString> getScores() {
        return scores;
    }

    public ScoreboardString getScore(int score) {
        return scores.get(score);
    }
}
