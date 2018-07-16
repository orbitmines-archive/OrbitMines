package com.orbitmines.spigot.api.handlers.achievements;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.AchievementsData;
import com.orbitmines.spigot.api.handlers.data.LootData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;

public abstract class AchievementHandler {

    protected Achievement achievement;

    public AchievementHandler(Achievement achievement) {
        this.achievement = achievement;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public abstract boolean hasCompleted(OMPlayer player);

    public String getName(OMPlayer omp) {
        return achievement.getName();
    }

    public String getDescription(OMPlayer omp) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < achievement.getDescription().length; i++) {
            if (i != 0)
                stringBuilder.append(" ");

            stringBuilder.append(omp.lang(achievement.getDescription()[i]));
        }

        return stringBuilder.toString();
    }

    public ItemBuilder getItemBuilder(OMPlayer omp) {
        boolean completed = alreadyCompletedAchievement(omp);

        ItemBuilder item = new ItemBuilder(completed ? Material.EXP_BOTTLE : Material.GLASS_BOTTLE, 1, 0, (completed ? "§a§l" : "§c§l") + achievement.getName());
        item.addLore("");

        for (Message line : achievement.getDescription()) {
            item.addLore("§7§o" + omp.lang(line));
        }

        item.addLore("");
        item.addLore("§7Rewards");

        for (Loot.Instance loot : achievement.getRewards()) {
            item.addLore("§7- " + loot.getLoot().getDisplayName(loot.getCount()));
        }

        if (completed && achievement.shouldShowProgressOnComplete()) {
            item.addLore("");
            AchievementsData data = getData(omp);
            item.addLore("§7§o" + NumberUtils.locale(data.getProgress(achievement)) + " " + achievement.completedProgress(data.getProgress(achievement)));
        }

        return item;
    }

    public void complete(OMPlayer omp, boolean notify) {
        if (alreadyCompletedAchievement(omp))
            return;

        Loot.Instance[] rewards = achievement.getRewards();

        if (notify) {
            omp.sendMessage("§5§m---------------------------------------------");
            omp.sendMessage("  §d§lACHIEVEMENT " + omp.lang("VOLTOOID", "COMPLETED"));
            omp.sendMessage("");
            omp.sendMessage("  §e§l" + getName(omp));
            omp.sendMessage("    §7" + getDescription(omp));
            omp.sendMessage("");
            omp.sendMessage("  §7Reward: " + lootToString());
            omp.sendMessage("§5§m---------------------------------------------");

            omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
        }

        getData(omp).complete(achievement);

        LootData lootData = (LootData) omp.getData(Data.Type.LOOT);
        for (Loot.Instance loot : rewards) {
            lootData.add(loot.getLoot(), achievement.getRarity(), "", loot.getCount());
        }
    }

    private String lootToString() {
        StringBuilder stringBuilder = new StringBuilder();
        Loot.Instance[] rewards = achievement.getRewards();

        for (int i = 0; i < rewards.length; i++) {
            Loot.Instance loot = rewards[i];

            if (i != 0)
                stringBuilder.append("§7, ");

            stringBuilder.append(loot.getLoot().getDisplayName(loot.getCount()));
        }

        return stringBuilder.toString();
    }

    public boolean alreadyCompletedAchievement(OMPlayer omp) {
        return getData(omp).hasCompleted(achievement);
    }

    protected AchievementsData getData(OMPlayer omp) {
        return ((AchievementsData) omp.getData(Data.Type.ACHIEVEMENTS));
    }
}
