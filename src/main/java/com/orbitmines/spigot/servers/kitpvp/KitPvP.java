package com.orbitmines.spigot.servers.kitpvp;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.*;
import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.events.VoidDamageEvent;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverActionBar;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.handlers.kit.KitInteractive;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.leaderboard.podium.DefaultPodiumLeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.PersonalisedFloatingItem;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointSpawnpoint;
import com.orbitmines.spigot.servers.hub.gui.stats.ServerStatsGUI;
import com.orbitmines.spigot.servers.kitpvp.cmd.CommandPrismShop;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointLobbyKitInfo;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointMapBarrier;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointMapSpawnpoint;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointMapSpectatorSpawnpoint;
import com.orbitmines.spigot.servers.kitpvp.events.*;
import com.orbitmines.spigot.servers.kitpvp.handlers.*;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.ItemHoverActiveHandler;
import com.orbitmines.spigot.servers.kitpvp.handlers.gui.CoinBoosterGUI;
import com.orbitmines.spigot.servers.kitpvp.handlers.gui.KitPvPPrismSolarShopGUI;
import com.orbitmines.spigot.servers.kitpvp.handlers.gui.KitSelectorGUI;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.*;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import com.orbitmines.spigot.servers.kitpvp.runnables.PassiveRunnable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitPvP extends OrbitMinesServer {

    public static final int COINS_PER_KILL = 50;
    public static final int XP_PER_KILL = 10;

    public static KitItemBuilder PLAYER_TRACKER = new KitItemBuilder(null, Material.COMPASS, 1, "§c§lPlayer Tracker").addPassive(Passive.PLAYER_TRACKING, 1);

    private Map<Language, Kit> lobbyKit;
    private Map<Language, Kit> spectatorKit;

    private List<Location> spawnLocations;

    static {
        new LeaderBoard.Instantiator("KITPVP_KILLS") {
            @Override
            public LeaderBoard instantiate(Location location, String[] data) {
                return new DefaultPodiumLeaderBoard(location, data, 3, Table.KITPVP_PLAYERS, TableKitPvPPlayers.UUID, TableKitPvPPlayers.KILLS) {
                    @Override
                    public String getValue(CachedPlayer player, int count) {
                        return "§c§l" + NumberUtils.locale(count) + " " + (count == 1 ? "Kill" : "Kills");
                    }
                };
            }
        };
    }

    public KitPvP(OrbitMines orbitMines) {
        super(orbitMines, Server.KITPVP, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
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
        KitPvPMap.loadMaps(orbitMines.getWorldLoader());

        for (KitPvPMap map : KitPvPMap.getMaps()) {
            map.setupDataPoints();

            World world = map.getWorld();

            preventionSet.prevent(world,
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
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);

            /* Datapoints */
            List<Location> spawnLocations = ((KitPvPDataPointMapSpawnpoint) (map.getHandler().getDataPoint(KitPvPMapDataPointHandler.Type.SPAWNPOINT))).getSpawns();
            List<Location> spectatorLocations = ((KitPvPDataPointMapSpectatorSpawnpoint) (map.getHandler().getDataPoint(KitPvPMapDataPointHandler.Type.SPECTATOR_SPAWNPOINT))).getSpawns();
            List<Location> voteSigns = new ArrayList<>();//TODO

            map.setup(spawnLocations, spectatorLocations, voteSigns);

            for (Block block : ((KitPvPDataPointMapBarrier) (map.getHandler().getDataPoint(KitPvPMapDataPointHandler.Type.BARRIER))).getBarriers()) {
                for (int y = block.getY(); y < block.getWorld().getMaxHeight(); y++) {
                    block.getWorld().getBlockAt(block.getX(), y, block.getZ()).setType(Material.BARRIER);
                }
            }
        }

        /* Remove trapdoor from PreventionSet: we want players to be able to interact with trapdoors and buttons */
        ItemUtils.INTERACTABLE.remove(Material.LEVER);
        ItemUtils.INTERACTABLE.remove(Material.STONE_BUTTON);

        ItemUtils.INTERACTABLE.remove(Material.ACACIA_BUTTON);
        ItemUtils.INTERACTABLE.remove(Material.BIRCH_BUTTON);
        ItemUtils.INTERACTABLE.remove(Material.DARK_OAK_BUTTON);
        ItemUtils.INTERACTABLE.remove(Material.JUNGLE_BUTTON);
        ItemUtils.INTERACTABLE.remove(Material.OAK_BUTTON);
        ItemUtils.INTERACTABLE.remove(Material.SPRUCE_BUTTON);

        /* Start Map rotation */
        nextMapRotation();

        registerKits();

        /* Datapoints */
        spawnLocations = ((HubDataPointSpawnpoint) (orbitMines.getLobby().getHandler().getDataPoint(KitPvPLobbyDataPointHandler.Type.SPAWNPOINT))).getSpawns();

        Map<Location, KitPvPDataPointLobbyKitInfo.KitInfo> kitInfo = ((KitPvPDataPointLobbyKitInfo) (orbitMines.getLobby().getHandler().getDataPoint(KitPvPLobbyDataPointHandler.Type.KIT_INFO))).getKitInfo();
        for (Location location : kitInfo.keySet()) {
            KitPvPDataPointLobbyKitInfo.KitInfo info = kitInfo.get(location);

            PersonalisedFloatingItem npc = new PersonalisedFloatingItem(info.getIcon(), location.subtract(0, 2, 0)) {
                @Override
                public ScoreboardString[] getLines(OMPlayer player) {
                    KitPvPPlayer omp = (KitPvPPlayer) player;

                    return new ScoreboardString[] {
                            info::getDisplayName,
                            () -> "§7§o" + info.getDescription(omp.getLastSelected())
                    };
                }
            };

            npc.create();
        }
    }

    @Override
    public void onDisable() {

    }

    public boolean isSaturday() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
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
            new DamageByEntityEvent(this),
            new DamageEvent(this),
            new DeathEvent(this),
            new ExpChangeEvent(orbitMines),
            new InteractEvent(this),
            new ProjectileEvents(this),
            new RegainHealthEvent(),
            new SpectatorEvents()
        );

        /* Handle Active Actionbar cooldown timers. */
        new ItemHoverActiveHandler(this);
    }

    @Override
    protected void registerCommands() {
        new CommandPrismShop();
    }

    @Override
    protected void registerRunnables() {
        new PassiveRunnable(this);
//        new MapResetSignRunnable();
        //TODO
    }

    private void registerKits() {
        {
            new KitKnight(this);
            new KitArcher(this);
            new KitSoldier(this);
            new KitMage(this);
            new KitTank(this);
            new KitDrunk(this);
            new KitPyro(this);
            new KitBunny(this);
            new KitKing(this);
        }

        /* Player Tracker */
        new ItemHoverActionBar(PLAYER_TRACKER, false) {
            @Override
            public String getMessage(OMPlayer player, ItemStack itemStack) {
                KitPvPPlayer omp = (KitPvPPlayer) player;

                OMPlayer closest = WorldUtils.getClosestPlayer(omp, GameMode.SURVIVAL, true);

                if (closest == null)
                    return "§c§lNo players nearby.";

                return "§c§lTracking: " + closest.getRankPrefixColor().getChatColor() + "§l" + closest.getName(true) + "       §c§l" + omp.lang("Afstand", "Distance") + ": " + closest.getRankPrefixColor().getChatColor() + "§l" + String.format("%.1f", omp.getLocation().distance(closest.getLocation()));
            }

            @Override
            public void onEnter(OMPlayer omp, ItemStack item, int slot) {
                super.onEnter(omp, item, slot);
                omp.playSound(Sound.UI_BUTTON_CLICK);
            }
        };

        /* Lobby Kit */
        for (Language language : Language.values()) {
            KitInteractive kit = new KitInteractive(language.toString());

            {
                ItemBuilder item = new ItemBuilder(Material.NAME_TAG, 1, "§1 ");

                kit.setItem(3, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        //TODO OPEN TELEPORTER
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§e§lTeleporter§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL, 1, "§2 ");

                kit.setItem(5, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        ((KitPvPPlayer) omp).setSpectator(false);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
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
                ItemBuilder item = new ItemBuilder(Material.EMERALD, 1, "§3 ");

                kit.setItem(1, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new ServerStatsGUI(omp, Server.KITPVP).open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§a§lStats§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.PRISMARINE_SHARD, 1, "§4 ");

                kit.setItem(3, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new KitPvPPrismSolarShopGUI().open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§9§lPrism Shop§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, "§5 ").addFlag(ItemFlag.HIDE_ATTRIBUTES);

                kit.setItem(4, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new KitSelectorGUI(KitPvP.this).open(omp);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                PlayerUtils.updateInventory(omp.getPlayer());
                            }
                        }.runTaskLater(orbitMines, 1);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§c§lKit Selector§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.GOLD_NUGGET, 1, "§6 ");

                kit.setItem(5, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        if (CoinBooster.ACTIVE == null)
                            new CoinBoosterGUI().open(omp);
                }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§6§lCoin Booster§r §8- §e§l" + (CoinBooster.ACTIVE == null ? omp.lang("Rechtermuisklik", "Right Click") : CoinBooster.ACTIVE.getType().getMultiplier() + "x§r §8- §b§l" + TimeUtils.fromTimeStamp(CoinBooster.ACTIVE.getTimer().getRemainingTicks() * 50, omp.getLanguage()));
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL, 1, "§7 ");

                kit.setItem(7, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        orbitMines.getServerSelectors().get(omp.getLanguage()).open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§3§lServer Selector§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_CHEST, 1, "§8 ");

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
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§9§lCosmetic Perks§r §8- §a§l" + omp.lang("Binnenkort", "Coming Soon");
//                        return "§9§lCosmetic Perks§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            kit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));

            //TODO COSMETIC HELMET
            kit.setHelmet(new ItemBuilder(Material.WHITE_STAINED_GLASS, 1, "§7Helmet"));

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
                    () -> " " + (omp.getSelectedKit() != null ? omp.getSelectedKit().getHandler().getDisplayName() + " §a§lLvl " + omp.getSelectedKit().getLevel() : "§fNone"),
                    () -> " ",
                    () -> "§6§lCoins",
                    () -> " " + NumberUtils.locale(omp.getCoins()),
                    () -> "  ",
                    () -> "§c§lKills",
                    () -> " " + (omp.getSelectedKit() == null ? "" : NumberUtils.locale(omp.getKitData(omp.getSelectedKit().getHandler()).getKills()) + " §7/ ") + NumberUtils.locale(omp.getKills()) + " ",
                    () -> "   ",
                    () -> "§4§lDeaths",
                    () -> " " + (omp.getSelectedKit() == null ? "" : NumberUtils.locale(omp.getKitData(omp.getSelectedKit().getHandler()).getDeaths()) + " §7/ ") + NumberUtils.locale(omp.getDeaths()) + "  ",
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
//          TODO
//        BossBar bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
//
//        /* Start Timer for the next Map */
//        KitPvPMap.TIMER = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.HOUR, 1), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, 1)) {
//            @Override
//            public void onInterval() {
//                //TODO update VOTE SIGN TIMER
//
//                float progress = getProgress();
//                if (progress > 0.05f) /* 3 minutes */
//                    return;
//
//                bossBar.setProgress(progress / 0.05f); /* 3 minutes */
//                bossBar.setTitle("§7§lMap Switch §8§l|| §c§l" + getNext().getName() + " §8§l|| §c§l" + TimeUtils.fromTimeStamp(getRemainingTicks() * 50, Language.ENGLISH));
//
//                /* 'Tick' sound every second, the last 10 seconds */
//                boolean tickSound = getRemainingTicks() <= 200;
//
//                /* Add Players to BossBar */
//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    if (!bossBar.getPlayers().contains(player))
//                        bossBar.addPlayer(player);
//
//                    if (tickSound)
//                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                bossBar.removeAll();
//                nextMapRotation();
//            }
//        };
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
