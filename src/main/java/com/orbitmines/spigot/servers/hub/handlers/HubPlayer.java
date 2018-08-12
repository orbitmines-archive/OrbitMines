package com.orbitmines.spigot.servers.hub.handlers;

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.FriendsData;
import com.orbitmines.spigot.api.handlers.data.SettingsData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.hub.Hub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class HubPlayer extends OMPlayer {

    protected static List<HubPlayer> players = new ArrayList<>();

    private final Hub hub;

    public HubPlayer(Hub hub, Player player) {
        super(player);

        this.hub = hub;
    }

    @Override
    protected void onLogin() {
        players.add(this);

        setScoreboard(new Hub.Scoreboard(orbitMines, this));

        hub.getLobbyKit(this).setItems(this);
        player.getInventory().setHeldItemSlot(4);

//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                AdvancementMessage a = new AdvancementMessage("redstone_block", "§8§lOrbit§7§lMines §7Advancement Message :D");
//                a.send(HubPlayer.this);
//            }
//        }.runTaskLater(orbitMines, 40);

        //TODO COSMETIC HELMET
        player.getInventory().setHelmet(new ItemBuilder(Material.WHITE_STAINED_GLASS, 1, "§7Helmet").build());

//        {
//            Location location = new Location(hub.getVoidWorld(), 0, 70, 0);
//
//            player.teleport(location);
//            freeze(Freezer.ARMORSTAND_RIDE, location.clone());
//
////            {
////                FloatingHeadBlock block = new FloatingHeadBlock(0, 90, new ItemBuilder(Material.STAINED_CLAY, 1, 14), location.clone().add(-2, 1, -2), true, new ArmorStandNpc.ClickAction() {
////                    @Override
////                    public void click(PlayerInteractAtEntityEvent event, OMPlayer player, ArmorStandNpc item) {
////
////                    }
////                });
////                block.setCustomName("§7§lNederlands");
////                block.setCustomNameVisible(true);
////                block.spawn(player);
////            }
////            {
////                FloatingHeadBlock block = new FloatingHeadBlock(0, 90, new ItemBuilder(Material.STAINED_CLAY, 1, 11), location.clone().add(2, 1, -2), true, new ArmorStandNpc.ClickAction() {
////                    @Override
////                    public void click(PlayerInteractAtEntityEvent event, OMPlayer player, ArmorStandNpc item) {
////
////                    }
////                });
////                block.setCustomName("§7§lEnglish");
////                block.setCustomNameVisible(true);
////                block.spawn(player);
////            }
//            {
//                FloatingHeadBlock block = new FloatingHeadBlock(0, 90, new ItemBuilder(Material.STAINED_CLAY, 1, 4), location.clone().add(-2, 1, -2), true, new ArmorStandNpc.ClickAction() {
//                    @Override
//                    public void click(PlayerInteractAtEntityEvent event, OMPlayer player, ArmorStandNpc item) {
//
//                    }
//                });
//                block.setCustomName("§e§lAlpha");
//                block.setCustomNameVisible(true);
//                block.spawn(player);
//            }
//            {
//                FloatingHeadBlock block = new FloatingHeadBlock(0, 90, new ItemBuilder(Material.STAINED_CLAY, 1, 11), location.clone().add(2, 1, -2), true, new ArmorStandNpc.ClickAction() {
//                    @Override
//                    public void click(PlayerInteractAtEntityEvent event, OMPlayer player, ArmorStandNpc item) {
//
//                    }
//                });
//                block.setCustomName("§9§lBeta");
//                block.setCustomNameVisible(true);
//                block.spawn(player);
//            }
//            {
//                FloatingHeadBlock block = new FloatingHeadBlock(0, 90, new ItemBuilder(Material.STAINED_CLAY, 1, 14), location.clone().add(0, 1, -3), true, new ArmorStandNpc.ClickAction() {
//                    @Override
//                    public void click(PlayerInteractAtEntityEvent event, OMPlayer player, ArmorStandNpc item) {
//
//                    }
//                });
//                block.setCustomName("§c§lOmega");
//                block.setCustomNameVisible(true);
//                block.spawn(player);
////                new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
////                    @Override
////                    public void run() {
////                        block.setLocation(block.getLocation().add(0, 0, 0.1));
////                        block.getArmorStand().teleport(block.getLocation());
////                    }
////                };
//            }
//            {
//                Hologram hologram = new Hologram(location.clone().add(0, 3, -5), true);
//
//                hologram.addLine("§8§lOrbit§7§lMines §6§lFirst Login");
//                hologram.addLine("§7§o2/3 - Race Selection");
//                hologram.addLine("§7");
//                hologram.addLine("§7§lPlease select one of the following races");
//
//                hologram.create(player);
//            }
//        }

        updatePlayerVisibility(OMPlayer.getPlayers());

        for (HubPlayer omp : HubPlayer.getHubPlayers()) {
            omp.updatePlayerVisibility(Collections.singletonList(this));
        }
    }

    @Override
    protected void onLogout() {

        players.remove(this);
    }

    @Override
    protected void onFirstLogin() {

    }

    @Override
    public void on2FALogin() {
        super.on2FALogin();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isFirstLogin())
                    sendMessage("Server", Color.BLUE, "Welkom op §8§lOrbit§7§lMines§7, " + getName() + "§7. Gebruik de §3§lEnder Pearl§7 om een server te joinen.", "Welcome to §8§lOrbit§7§lMines§7, " + getName() + "§7. Use the §3§lEnder Pearl§7 to join a server.");
                else
                    sendMessage("Server", Color.BLUE, "Welkom terug, " + getName() + "§7. Gebruik de §3§lEnder Pearl§7 om een server te joinen.", "Welcome back, " + getName() + "§7. Use the §3§lEnder Pearl§7 to join a server.");
            }
        }.runTaskLater(orbitMines, 20);
    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    @Override
    public void updateLanguage() {
        super.updateLanguage();

        /* Update Inventory */
        hub.getLobbyKit(this).setItems(this);
    }

    @Override
    public void updateSettings(Settings settings, SettingsType settingsType) {
        super.updateSettings(settings, settingsType);

        /* Update player visibility after settings are changed. */
        if (settings == Settings.PLAYER_VISIBILITY)
            updatePlayerVisibility(OMPlayer.getPlayers());
    }

    public void updatePlayerVisibility(Collection<? extends OMPlayer> players) {
        switch (((SettingsData) getData(Data.Type.SETTINGS)).getSettings().get(Settings.PLAYER_VISIBILITY)) {

            case ENABLED:
                for (OMPlayer omp : players) {
                    if (omp == this)
                        continue;

                    this.player.showPlayer(omp.getPlayer());
                }
                break;
            case ONLY_FRIENDS: {
                List<UUID> friends = ((FriendsData) getData(Data.Type.FRIENDS)).getFriends(true);
                for (OMPlayer omp : players) {
                    if (omp == this)
                        continue;
                    //TODO DEPRECATED
                    if ((omp.getStaffRank() != StaffRank.NONE && omp.getStaffRank() != StaffRank.ADMIN) || friends.contains(omp.getUUID()))
                        this.player.showPlayer(omp.getPlayer());
                    else
                        this.player.hidePlayer(omp.getPlayer());
                }
                break;
            }
            case ONLY_FAVORITE_FRIENDS: {
                List<UUID> friends = ((FriendsData) getData(Data.Type.FRIENDS)).getFavoriteFriends();
                for (OMPlayer omp : players) {
                    if (omp == this)
                        continue;

                    if ((omp.getStaffRank() != StaffRank.NONE && omp.getStaffRank() != StaffRank.ADMIN) || friends.contains(omp.getUUID()))
                        this.player.showPlayer(omp.getPlayer());
                    else
                        this.player.hidePlayer(omp.getPlayer());
                }
                break;
            }
            case DISABLED:
                for (OMPlayer omp : players) {
                    if (omp == this)
                        continue;

                    if ((omp.getStaffRank() != StaffRank.NONE && omp.getStaffRank() != StaffRank.ADMIN))
                        this.player.showPlayer(omp.getPlayer());
                    else
                        this.player.hidePlayer(omp.getPlayer());
                }
                break;
        }
    }

    /*


        HubPlayer Getters


     */

    public static HubPlayer getPlayer(Player player) {
        for (HubPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        throw new IllegalStateException();
    }

    public static HubPlayer getPlayer(String name) {
        for (HubPlayer omp : players) {
            if (omp.getName(true).equalsIgnoreCase(name))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static HubPlayer getPlayer(UUID uuid) {
        for (HubPlayer omp : players) {
            if (omp.getUUID().toString().equals(uuid.toString()))
                return omp;
        }
        throw new IllegalStateException();
    }

    public static List<HubPlayer> getHubPlayers() {
        return players;
    }
}
