package com.orbitmines.spigot.api.handlers.achievements;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;

public class StoredProgressAchievement extends ProgressAchievement {

    public StoredProgressAchievement(Achievement achievement, int total) {
        super(achievement, total);
    }

    /* Override this if it is stored elsewhere */
    @Override
    public int getCurrent(OMPlayer omp) {
        return getData(omp).getProgress(achievement);
    }

    public void progress(OMPlayer omp, int count, boolean notifyOnComplete) {
        getData(omp).progress(achievement, count, notifyOnComplete);
    }

    @Override
    public ItemBuilder getItemBuilder(OMPlayer omp) {
        return super.getItemBuilder(omp).addLore("").addLore("§6" + NumberUtils.locale(getCurrent(omp)) + "§7/" + NumberUtils.locale(total));
    }
}
