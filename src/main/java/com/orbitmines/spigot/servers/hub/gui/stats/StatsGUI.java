package com.orbitmines.spigot.servers.hub.gui.stats;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.data.AchievementsData;
import com.orbitmines.spigot.api.handlers.data.PlayTimeData;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPData;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.LevelData;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalData;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Warp;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class StatsGUI extends GUI {

    private final OrbitMines orbitMines;
    private final OMPlayer player;

    public StatsGUI(OMPlayer player) {
        this.orbitMines = OrbitMines.getInstance();
        this.player = player;
        newInventory(54, "§0§lStats (" + player.getName(true) + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        add(1, 3, new EmptyItemInstance(new ItemBuilder(Material.SUNFLOWER, 1, "§e§l" + NumberUtils.locale(player.getSolars()) + " Solar" + (player.getSolars() == 1 ? "" : "s")).build()));

        add(1, 4, new ItemInstance(getItem(player, null, true)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new AchievementsGUI(player).open(omp);
            }
        });

        add(1, 5, new EmptyItemInstance(new ItemBuilder(Material.PRISMARINE_SHARD, 1, "§9§l" + NumberUtils.locale(player.getPrisms()) + " Prism" + (player.getPrisms() == 1 ? "" : "s")).build()));

        add(4, 4, new ItemInstance(getItem(player, Server.SURVIVAL, true)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new ServerStatsGUI(player, Server.SURVIVAL).open(omp);
            }
        });

        add(3, 3, new ItemInstance(getItem(player, Server.KITPVP, true)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new ServerStatsGUI(player, Server.KITPVP).open(omp);
            }
        });

        EmptyItemInstance item = new EmptyItemInstance(new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1, new Message("§cOnbekende Galaxies", "§cUnknown Galaxies").lang(player.getLanguage())).build());

        add(3, 1, item);
        add(3, 5, item);
        add(3, 7, item);
        add(4, 2, item);
        add(4, 6, item);

        return true;
    }

    public static ItemStack getItem(OMPlayer player, Server server, boolean generalStats) {
        if (server == null) {
            PlayerSkullBuilder item = new PlayerSkullBuilder(() -> player.getName(true), 1, player.getName());
            item.addLore("§7Rank: " + ((player.getStaffRank() == StaffRank.NONE || player.getStaffRank() == StaffRank.ADMIN) ? player.getVipRank().getDisplayName() : (player.getVipRank() == VipRank.NONE ? player.getStaffRank().getDisplayName() : player.getStaffRank().getDisplayName() + " §7/ " + player.getVipRank().getDisplayName())));

            OrbitMinesServer handler = OrbitMines.getInstance().getServerHandler();
            User user = handler.getDiscord().getLinkedUser(handler.getToken(), player.getUUID());
            item.addLore("§7Discord: " + (user != null ? "§9§l" + user.getName() + "#" + user.getDiscriminator() : StaffRank.NONE.getDisplayName()));

            PlayTimeData data = (PlayTimeData) player.getData(Data.Type.PLAY_TIME);
            /* Update Play Time */
            data.load();

            long totalTimePlayed = 0;
            for (Server server2 : Server.values()) {
                totalTimePlayed += data.getPlayTime().get(server2);
            }
            item.addLore("§7" + player.lang("Tijd Gespeeld", "Time Played") + ": §a§l" + TimeUtils.limitTimeUnitBy(totalTimePlayed * 1000, TimeUnit.HOURS, player.getLanguage()));

            item.addLore("§7" + player.lang("Lid Sinds", "Member Since") + ": §a§l" + DateUtils.SIMPLE_FORMAT.format(DateUtils.parse(DateUtils.FORMAT, CachedPlayer.getPlayer(player.getUUID()).getFirstLogin())));
            item.addLore("");

            VoteData voteData = (VoteData) player.getData(Data.Type.VOTES);
            item.addLore("§7Votes in " + DateUtils.getMonth() + " " + DateUtils.getYear() + ": §9§l" + voteData.getVotes());
            item.addLore("§7" + player.lang("Vote Totaal", "Total Votes") +  ": §9§l" + NumberUtils.locale(voteData.getTotalVotes()));

            AchievementsData achievementsData = (AchievementsData) player.getData(Data.Type.ACHIEVEMENTS);

            int achievementCount = 0;
            int totalAchievementCount = 0;
            for (Server server2 : Server.values()) {
                Achievement[] achievements = server2.achievementValues();
                totalAchievementCount += achievements.length;

                for (Achievement achievement : achievements) {
                    if (achievementsData.hasCompleted(achievement))
                        achievementCount++;
                }
            }

            item.addLore("");
            item.addLore("§7Achievements: §d§l" + achievementCount + "§7§l / " + totalAchievementCount);
            item.addLore("");
            item.addLore("§7Cosmetics");
            item.addLore("§7  §a§lComing Soon");
//            item.addLore("§7  Hats: §6§l" + "0" /* TODO */ + "§7§l / " + "0");
//            item.addLore("§7  Gadgets: §b§l" + "0" /* TODO */ + "§7§l / " + "0");

            if (generalStats) {
                item.addLore("");
                item.addLore(player.lang("§aKlik hier voor achievements.", "§aClick here for achievements."));
            }

            return item.build();
        }

        PlayTimeData data = (PlayTimeData) player.getData(Data.Type.PLAY_TIME);

        ItemBuilder item = new ItemBuilder(null, 1, "§8§lOrbit§7§lMines " + server.getDisplayName());
        item.addLore("§7" + player.lang("Tijd Gespeeld", "Time Played") + ": §a§l" + TimeUtils.limitTimeUnitBy(data.getPlayTime().get(server) * 1000, TimeUnit.HOURS, player.getLanguage()));
        item.addLore("");

        switch (server) {

            case KITPVP:
                item.setMaterial(Material.IRON_SWORD);
                item.addFlag(ItemFlag.HIDE_ATTRIBUTES);

                KitPvPData kitPvP = (KitPvPData) player.getData(Data.Type.KITPVP);

                int[] kits = new int[4];

                for (KitPvPData.KitData kitData : kitPvP.getAllKitData()) {
                    int level = kitData.getUnlockedLevel();

                    kits[level] = kits[level] + 1;
                }

                KitPvPPlayer dummy = new KitPvPPlayer(null, null) {
                    @Override
                    protected KitPvPData getData() {
                        return kitPvP;
                    }
                };
                LevelData levelData = dummy.getLevelData();
                levelData.update(false);

                item.addLore("§7Coins: §6§l" + NumberUtils.locale(kitPvP.getCoins()));
                item.addLore("§7Level: " + levelData.getColor() + "§l" + levelData.getLevel());
                item.addLore((levelData.getLevel() == LevelData.maxLevel ? levelData.getColor() + "Max Level" : "§7Next level: §e§l" + NumberUtils.locale(levelData.getCurrentLevelXp()) + " XP §7§l/ " + NumberUtils.locale(levelData.getNextLevelXp())));
                item.addLore("");
                item.addLore("§7Kills: §c§l" + NumberUtils.locale(kitPvP.getKills()));
                item.addLore("§7Deaths: §4§l" + NumberUtils.locale(kitPvP.getDeaths()));
                item.addLore("§7Best streak: §5§l" + NumberUtils.locale(kitPvP.getBestStreak()));
                item.addLore("§7Damage dealt: §c§l" + NumberUtils.biggestCountUnit(kitPvP.getDamageDealt()));
                item.addLore("");
                item.addLore("§7Kits");
                for (int i = 0; i < kits.length; i++) {
                    if (kits[i] == 0)
                        continue;

                    if (i == 0)
                        item.addLore("  §7Locked: §4§l" + kits[i]);
                    else
                        item.addLore("  §7Level " + i + ": §a§l" + kits[i]);
                }
                item.addLore("");
                break;
            case PRISON:
                break;
            case CREATIVE:
                break;
            case HUB:
                break;
            case SURVIVAL: {
                item.setMaterial(Material.STONE_HOE);
                item.addFlag(ItemFlag.HIDE_ATTRIBUTES);

                SurvivalData survival = (SurvivalData) player.getData(Data.Type.SURVIVAL);

                item.addLore("§7Credits: §2§l" + NumberUtils.locale(survival.getEarthMoney()));
                item.addLore("§7" + player.lang("Claimblock Totaal", "Total Claimblocks") + ": §9§l" + NumberUtils.locale(survival.getClaimBlocks()));
                item.addLore("");
                item.addLore("§7Claims: §a§l" + NumberUtils.locale(Claim.getClaimCount(player.getUUID())));
                item.addLore("");
                item.addLore("§7Homes: " + Home.COLOR.getChatColor() + "§l" + NumberUtils.locale(Home.getHomeCount(player.getUUID())) + "§7§l / " + NumberUtils.locale(survival.getHomesAllowed(player)));
                item.addLore("§7Warps: " + Warp.COLOR.getChatColor() + "§l" + NumberUtils.locale(Warp.getWarpCount(player.getUUID())) + "§7§l / " + NumberUtils.locale(Warp.Type.values().length));
                item.addLore("");
                item.addLore("§7Back Charges: §6§l" + NumberUtils.locale(survival.getBackCharges()));
                item.addLore("");
                break;
            }
            case SKYBLOCK:
                break;
            case FOG:
                break;
            case MINIGAMES:
                break;
            case UHSURVIVAL:
                break;
        }

        AchievementsData achievementsData = (AchievementsData) player.getData(Data.Type.ACHIEVEMENTS);

        int achievementCount = 0;
        Achievement[] achievements = server.achievementValues();
        for (Achievement achievement : achievements) {
            if (achievementsData.hasCompleted(achievement))
                achievementCount++;
        }

        item.addLore("§7Achievements: §d§l" + achievementCount + "§7§l / " + achievements.length);

        if (generalStats) {
            item.addLore("");
            item.addLore(player.lang("§aKlik hier voor achievements.", "§aClick here for achievements."));
        }

        return item.build();
    }
}
