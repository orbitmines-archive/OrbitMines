package com.orbitmines.spigot.servers.hub.handlers;

import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.hub.Hub;
import net.firefang.ip2c.IpUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        player.setGameMode(GameMode.ADVENTURE);

//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                AdvancementMessage a = new AdvancementMessage("redstone_block", "§7§lOrbit§8§lMines §7Advancement Message :D");
//                a.send(HubPlayer.this);
//            }
//        }.runTaskLater(orbitMines, 40);

        //TODO COSMETIC HELMET
        player.getInventory().setHelmet(new ItemBuilder(Material.STAINED_GLASS, 1, 0, "§7Helmet").build());

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

        if (language != Language.DUTCH)
            return;

        String country = IpUtils.getCountry(getPlayer());
        if (country == null || (!country.equals("Netherlands") && !country.equals("Belgium") && !country.equals("Luxembourg"))) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendMessage("");
                    sendMessage("Language", Color.RED, "§c§lIMPORTANT:");
                    sendMessage("Language", Color.RED, "§7Welcome to OrbitMines, " + getName() + "§7!");
                    sendMessage("Language", Color.RED, "§7It seems like you're not from the Benelux. We have added an option for players like you to change the language of all messages on our server. Click on the §c§lRedstone Torch§7, then click on the §c§lBanner§7 in order to switch to §c§lEnglish§7.");
                }
            }.runTaskLater(orbitMines, 30);
        }
    }

    @Override
    protected void onLogout() {

        players.remove(this);
    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
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
