package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.Server;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableAchievements;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.StoredProgressAchievement;

import java.util.*;

import static com.orbitmines.api.database.tables.TableLoot.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class AchievementsData extends Data {

    private List<Achievement> completedCache;
    private Map<Achievement, Integer> progress;

    /*
        Achievements keep progressing even if completed for display.
        If the achievements are changed (rewards/changes to difficulty) all players that have completed the achievement cannot do it again.
     */

    public AchievementsData(UUID uuid) {
        super(Table.ACHIEVEMENTS, Type.ACHIEVEMENTS, uuid);

        this.completedCache = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    @Override
    public void load() {
        completedCache.clear();
        progress.clear();

        List<Map<Column, String>> entries = Database.get().getEntries(table, new Where(UUID, getUUID().toString()));

        for (Map<Column, String> entry : entries) {
            Server server = Server.valueOf(entry.get(TableAchievements.SERVER));
            Achievement achievement = server.achievementValueOf(entry.get(TableAchievements.ACHIEVEMENT));
            boolean completed = "1".equals(entry.get(TableAchievements.COMPLETED));
            int progress = Integer.parseInt(entry.get(TableAchievements.PROGRESS));

            this.progress.put(achievement, progress);

            if (completed)
                completedCache.add(achievement);
        }
    }

    public void complete(Achievement achievement) {
        //TODO MiniGames; can receive achievements

        if (progress.containsKey(achievement))
            updateProgress(achievement);

        completedCache.add(achievement);

        updateCompleted(achievement);
    }

    public void progress(Achievement achievement, int count, boolean notifyOnComplete) {
        //TODO MiniGames; can receive achievements

        boolean alreadyCompleted = hasCompleted(achievement);

        OMPlayer omp = OMPlayer.getPlayer(uuid);

        if (omp == null)
            throw new IllegalStateException("StatsAchievements#progress cannot be called if the player is not online.");

        if (!progress.containsKey(achievement))
            progress.put(achievement, count);
        else
            progress.put(achievement, progress.get(achievement) + count);

        StoredProgressAchievement handler = ((StoredProgressAchievement) achievement.getHandler());

        if (progress.get(achievement) > handler.getTotal())
            progress.put(achievement, handler.getTotal());

        if (!alreadyCompleted && handler.hasCompleted(omp))
            achievement.getHandler().complete(omp, notifyOnComplete);
        else
            updateProgress(achievement);
    }

    public int getProgress(Achievement achievement) {
        return progress.getOrDefault(achievement, 0);
    }

    public boolean hasCompleted(Achievement achievement) {
        return completedCache.contains(achievement);
    }

    private void updateCompleted(Achievement achievement) {
        if (!Database.get().contains(table, TableAchievements.COMPLETED, new Where(TableAchievements.UUID, uuid.toString()), new Where(TableAchievements.SERVER, achievement.getServer().toString()), new Where(TableAchievements.ACHIEVEMENT, achievement.toString()))) {
            Database.get().insert(table, uuid.toString(), achievement.getServer().toString(), achievement.toString(), "1", progress.getOrDefault(achievement, 0) + "");
        } else {
            Database.get().update(table, new Set(TableAchievements.COMPLETED, true), new Where(TableAchievements.UUID, uuid.toString()), new Where(TableAchievements.SERVER, achievement.getServer().toString()), new Where(TableAchievements.ACHIEVEMENT, achievement.toString()));
        }
    }

    private void updateProgress(Achievement achievement) {
        if (!Database.get().contains(table, TableAchievements.PROGRESS, new Where(TableAchievements.UUID, uuid.toString()), new Where(TableAchievements.SERVER, achievement.getServer().toString()), new Where(TableAchievements.ACHIEVEMENT, achievement.toString()))) {
            Database.get().insert(table, uuid.toString(), achievement.getServer().toString(), achievement.toString(), "0", progress.getOrDefault(achievement, 0) + "");
        } else {
            Database.get().update(table, new Set(TableAchievements.PROGRESS, progress.getOrDefault(achievement, 0)), new Where(TableAchievements.UUID, uuid.toString()), new Where(TableAchievements.SERVER, achievement.getServer().toString()), new Where(TableAchievements.ACHIEVEMENT, achievement.toString()));
        }
    }
}
