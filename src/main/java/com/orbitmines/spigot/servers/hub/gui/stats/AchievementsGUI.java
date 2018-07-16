package com.orbitmines.spigot.servers.hub.gui.stats;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchievementsGUI extends GUI {


    private final int ACHIEVEMENTS_PER_PAGE = 36;
    private final int NEW_PER_PAGE = ACHIEVEMENTS_PER_PAGE / 9;

    private final List<Achievement> achievements;

    private final OMPlayer player;
    private int page;

    public AchievementsGUI(OMPlayer player) {
        this(player, 0);
    }

    public AchievementsGUI(OMPlayer player, int page) {
        this.player = player;
        this.page = page;

        achievements = new ArrayList<>();

        for (Server server : Server.values()) {
            achievements.addAll(Arrays.asList(server.achievementValues()));
        }

        newInventory(54, "§0§lStats (" + player.getName(true) + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        add(1, 3, new ItemInstance(new PlayerSkullBuilder(() -> "Lime Arrow Left", 1, omp.lang("§a« Terug naar Stats", "§a« Back to General Stats")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUzNDc0MjNlZTU1ZGFhNzkyMzY2OGZjYTg1ODE5ODVmZjUzODlhNDU0MzUzMjFlZmFkNTM3YWYyM2QifX19").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new StatsGUI(player).open(omp);
            }
        });

        add(1, 4, new EmptyItemInstance(StatsGUI.getItem(player, null, false)));

        int slot = 18;

        for (Achievement achievement : getAchievementsForPage(achievements)) {
            if (achievement != null) {
                ItemBuilder builder = achievement.getHandler().getItemBuilder(player);
                builder.setDisplayName(builder.getDisplayName() + " §7(" + achievement.getServer().getDisplayName() + "§7)");
                add(slot, new EmptyItemInstance(builder.build()));
            } else {
                clear(slot);
            }

            slot++;
        }

        if (page != 0)
            add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Magenta Arrow Left", 1, omp.lang("§7« Meer Achievements", "§7« More Achievements")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRiZDhlYzQ2MTkyYTk1NWM2YmQ2NTFhYjRkODZmOTg2N2U0ZDRiZDk5YWYyMWNhOTJlYzlkMjZmODZkYTkxIn19fQ==").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page--;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 0);

        if (canHaveMorePages(achievements))
            add(5, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Magenta Arrow Right", 1, omp.lang("§7Meer Achievements »", "§7More Achievements »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA3OGUxNjg5MmEyYzljMDIzMDUyMTNjY2U2MDQ4M2FiN2FkYTQ3ZjEzYWY5YjhkYTg3Y2U2M2RkODM0NyJ9fX0=").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page++;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(5, 8);

        return true;
    }

    private Achievement[] getAchievementsForPage(List<Achievement> achievements) {
        Achievement[] pageAchievements = new Achievement[ACHIEVEMENTS_PER_PAGE];

        for (int i = 0; i < ACHIEVEMENTS_PER_PAGE; i++) {
            if (achievements.size() > i)
                pageAchievements[i] = achievements.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < ACHIEVEMENTS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = ACHIEVEMENTS_PER_PAGE + check + (NEW_PER_PAGE * i);
                        pageAchievements[j] = achievements.size() > next ? achievements.get(next) : null;
                    } else {
                        pageAchievements[j] = pageAchievements[j + 1];
                    }
                }
            }
        }

        return pageAchievements;
    }

    private boolean canHaveMorePages(List<Achievement> achievements) {
        int achievementAmount = achievements.size();

        if (achievementAmount <= ACHIEVEMENTS_PER_PAGE)
            return false;

        int maxPage = achievementAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (achievementAmount - ACHIEVEMENTS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (achievementAmount - ACHIEVEMENTS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }
}
