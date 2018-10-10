package com.orbitmines.spigot.api.handlers.achievements;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;

public abstract class ProgressAchievement extends AchievementHandler {

    protected int total;

    public ProgressAchievement(Achievement achievement, int total) {
        super(achievement);

        this.total = total;
    }

    public abstract int getCurrent(OMPlayer omp);

    @Override
    public boolean hasCompleted(OMPlayer omp) {
        return getProgress(omp) >= 1;
    }

    @Override
    public String getName(OMPlayer omp) {
        return super.getName(omp) + " §d§l" + NumberUtils.locale(getCurrent(omp)) + " §7§l/ " + NumberUtils.locale(total) + " (§d§l" + ((int) (getProgress(omp) * 100)) + "%§7§l)";
    }

    public int getTotal() {
        return total;
    }

    /* Return float between 0 and 1 */
    public float getProgress(OMPlayer omp) {
        return (float) getCurrent(omp) / (float) total;
    }
}
