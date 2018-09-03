package com.orbitmines.spigot.servers.kitpvp;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.events.VoidDamageEvent;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverActionBar;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.handlers.kit.KitInteractive;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.hub.gui.stats.ServerStatsGUI;
import com.orbitmines.spigot.servers.kitpvp.events.ExpChangeEvent;
import com.orbitmines.spigot.servers.kitpvp.events.RegainHealthEvent;
import com.orbitmines.spigot.servers.kitpvp.events.SpectatorEvents;
import com.orbitmines.spigot.servers.kitpvp.handlers.CoinBooster;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPMap;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitPvP extends OrbitMinesServer {

    public static final int COINS_PER_KILL = 50;
    public static final int XP_PER_KILL = 10;

    private Map<Language, Kit> lobbyKit;
    private Map<Language, Kit> spectatorKit;

    private List<Location> spawnLocations;

    public KitPvP(OrbitMines orbitMines) {
        super(orbitMines, Server.KITPVP, null);//todo
    }

    @Override
    public void onEnable() {
        lobbyKit = new HashMap<>();
        spectatorKit = new HashMap<>();

        preventionSet.prevent(orbitMines.getLobby().getWorld(),
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.CHUNK_UNLOAD,
                PreventionSet.Prevention.CLICK_PLAYER_INVENTORY,
                PreventionSet.Prevention.ENTITY_INTERACTING,
                PreventionSet.Prevention.FOOD_CHANGE,
                PreventionSet.Prevention.ITEM_DROP,
                PreventionSet.Prevention.LEAF_DECAY,
                PreventionSet.Prevention.PLAYER_DAMAGE,
                PreventionSet.Prevention.ITEM_PICKUP,
                PreventionSet.Prevention.SWAP_HAND_ITEMS,
                PreventionSet.Prevention.WEATHER_CHANGE,
                PreventionSet.Prevention.MONSTER_EGG_USAGE,
                PreventionSet.Prevention.BUCKET_USAGE
        );

        /* Load & Create all Maps from Database */
        KitPvPMap.loadMaps();
        nextMapRotation();

        for (KitPvPMap map : KitPvPMap.getMaps()) {
            preventionSet.prevent(map.getWorld(),
                    PreventionSet.Prevention.BLOCK_BREAK,
                    PreventionSet.Prevention.BLOCK_INTERACTING,
                    PreventionSet.Prevention.BLOCK_PLACE,
                    PreventionSet.Prevention.LEAF_DECAY,
                    PreventionSet.Prevention.WEATHER_CHANGE,

                    PreventionSet.Prevention.ENTITY_INTERACTING,
                    PreventionSet.Prevention.ITEM_DROP,
                    PreventionSet.Prevention.ITEM_PICKUP,
                    PreventionSet.Prevention.MONSTER_EGG_USAGE,

                    PreventionSet.Prevention.FOOD_CHANGE,
                    PreventionSet.Prevention.BLOCK_SPREAD,
                    PreventionSet.Prevention.MONSTER_EGG_USAGE,
                    PreventionSet.Prevention.BUCKET_USAGE,
                    PreventionSet.Prevention.PHYSICAL_INTERACTING_EXCEPT_PLATES
            );
            map.getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        registerKits();

        /* Datapoints */
    }

    @Override
    public void onDisable() {

    }

    public Kit getLobbyKit(KitPvPPlayer omp) {
        return lobbyKit.get(omp.getLanguage());
    }

    public Kit getSpectatorKit(KitPvPPlayer omp) {
        return spectatorKit.get(omp.getLanguage());
    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new KitPvPPlayer(this, player);
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        return true;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return RandomUtils.randomFrom(spawnLocations);
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.SURVIVAL;
    }

    @Override
    public boolean format(CachedPlayer sender, OMPlayer receiver, Color color, String string, List<ComponentMessage.TempTextComponent> list) {
        return false;
    }

    @Override
    protected void registerEvents() {
        registerEvents(
            new VoidDamageEvent(orbitMines.getLobby().getWorld()) {
                @Override
                public Location getRespawnLocation(Player player) {
                    return getSpawnLocation(player);
                }
            },
            new ExpChangeEvent(orbitMines),
            new RegainHealthEvent(),
            new SpectatorEvents()
        );
    }

    @Override
    protected void registerCommands() {
        //TODO
    }

    @Override
    protected void registerRunnables() {
        //TODO
    }

    private void registerKits() {

        /* Lobby Kit */
        for (Language language : Language.values()) {
            KitInteractive kit = new KitInteractive(language.toString());

            {
                ItemBuilder item = new ItemBuilder(Material.NAME_TAG, 1, "§f ");

                kit.setItem(3, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        //TODO OPEN TELEPORTER
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§e§lTeleporter§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL, 1, "§f ");

                kit.setItem(5, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        ((KitPvPPlayer) omp).setSpectator(false);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§3§l" + omp.lang("Terug naar Spawn", "Back to Spawn") + "§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            kit.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false));

            spectatorKit.put(language, kit);
        }

        /* Lobby Kit */
        for (Language language : Language.values()) {
            KitInteractive kit = new KitInteractive(language.toString());

            {
//                kit.setItem(0, RULE_BOOK.get(language));
//
//                new ItemHoverActionBar(new ItemBuilder(Material.WRITTEN_BOOK, 1, "§f "), false) {
//                    @Override
//                    public String getMessage(OMPlayer omp) {
//                        return "§4§l" + omp.lang("Regels", "Rules") + "§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
//                    }
//                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.EMERALD, 1, "§f ");

                kit.setItem(1, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new ServerStatsGUI(omp, Server.KITPVP).open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§a§lStats§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.PRISMARINE_SHARD, 1, "§f ");

                kit.setItem(3, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        //TODO OPEN PRIMSSHOP
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§9§lPrism Shop§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, "§f ");

                kit.setItem(4, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        //TODO OPEN KIT SELECTOR
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§c§lKit Selector§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.SUNFLOWER, 1, "§f ");

                kit.setItem(5, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

//                        if (CoinBooster.ACTIVE != null)
//                            //open
                }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§6§lCoin Booster§r §8- §e§l" + (CoinBooster.ACTIVE == null ? omp.lang("Rechtermuisklik", "Right Click") : CoinBooster.ACTIVE.getType().getMultiplier() + "x§r §8- §c§l" + TimeUtils.fromTimeStamp(CoinBooster.ACTIVE.getTimer().getRemainingTicks() * 50, omp.getLanguage()));
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL, 1, "§f ");

                kit.setItem(7, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        orbitMines.getServerSelectors().get(omp.getLanguage()).open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§3§lServer Selector§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_CHEST, 1, "§f ");

                kit.setItem(8, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

//                    if (omp.canReceiveVelocity())
//                        TODO OPEN COSMETICS
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp) {
                        return "§9§lCosmetic Perks§r §8- §a§l" + omp.lang("Binnenkort", "Coming Soon");
//                        return "§9§lCosmetic Perks§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            kit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

            lobbyKit.put(language, kit);
        }
    }

    @Override
    public void setupNpc(String npcName, Location location) {

    }

    public static class Scoreboard extends DefaultScoreboard {

        public Scoreboard(OrbitMines orbitMines, KitPvPPlayer omp) {
            super(omp,
                    () -> orbitMines.getScoreboardAnimation().get(),
                    () -> "§m--------------",
                    () -> "",
                    () -> "§7§lKit",
                    () -> " " + (omp.getSelectedKit() != null ? omp.getSelectedKit().getHandler().getDisplayName() + " §e§l" + omp.getSelectedKit().getLevel() : "§fNONE"),
                    () -> " ",
                    () -> "§6§lCoins",
                    () -> " " + NumberUtils.locale(omp.getCoins()),
                    () -> "  ",
                    () -> "§c§lKills",
                    () -> " " + NumberUtils.locale(omp.getKills()) + " ",
                    () -> "   ",
                    () -> "§4§lDeaths",
                    () -> " " + NumberUtils.locale(omp.getDeaths()) + "  ",
                    () -> "    "

            );
        }

        @Override
        public boolean canBypassSettings() {
            return false;
        }
    }

    /* Map Rotation */
    private void nextMapRotation() {
        KitPvPMap previous = KitPvPMap.CURRENT;

        if (previous == null) {
            KitPvPMap.CURRENT = RandomUtils.randomFrom(KitPvPMap.getMaps());
        } else {
            KitPvPMap.CURRENT = getNext();

            /* Teleport all players in previous Map to the current one */
            for (Player player : previous.getWorld().getPlayers()) {
                KitPvPPlayer.getPlayer(player).teleportToMap();
            }

            /* Clear all votes */
            for (KitPvPMap map : KitPvPMap.getMaps()) {
                map.getVotes().clear();
            }
        }

        BossBar bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);

        /* Start Timer for the next Map */
        KitPvPMap.TIMER = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.HOUR, 1), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, 1)) {
            @Override
            public void onInterval() {
                //TODO update VOTE SIGN TIMER

                float progress = getProgress();
                if (progress > 0.05f) /* 3 minutes */
                    return;

                bossBar.setProgress(progress / 0.05f); /* 3 minutes */
                bossBar.setTitle("§7§lMap Switch §8§l|| §c§l" + getNext().getName() + " §8§l|| §c§l" + TimeUtils.fromTimeStamp(getRemainingTicks() * 50, Language.ENGLISH));

                /* 'Tick' sound every second, the last 10 seconds */
                boolean tickSound = getRemainingTicks() <= 200;

                /* Add Players to BossBar */
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!bossBar.getPlayers().contains(player))
                        bossBar.addPlayer(player);

                    if (tickSound)
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
                }
            }

            @Override
            public void onFinish() {
                bossBar.removeAll();
                nextMapRotation();
            }
        };
    }

    private KitPvPMap getNext() {
        KitPvPMap previous = KitPvPMap.CURRENT;

        int votes = 0;
        List<KitPvPMap> list = new ArrayList<>();

        for (KitPvPMap map : KitPvPMap.getMaps()) {
            int v = map.getVotes().size();

            if (v == 0 || v < votes)
                continue;

            if (v > votes) {
                votes = v;
                list.clear();
            }

            list.add(map);
        }

        if (list.size() != 0)
            return RandomUtils.randomFrom(list);

        list = new ArrayList<>(KitPvPMap.getMaps());
        list.remove(previous);

        return RandomUtils.randomFrom(list);
    }
}
