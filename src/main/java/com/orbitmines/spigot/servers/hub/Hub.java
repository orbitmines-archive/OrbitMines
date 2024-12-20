package com.orbitmines.spigot.servers.hub;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.*;
import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.events.VoidDamageEvent;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverActionBar;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.handlers.kit.KitInteractive;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointSpawnpoint;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointStaffHologram;
import com.orbitmines.spigot.servers.hub.gui.SettingsGUI;
import com.orbitmines.spigot.servers.hub.gui.discordgroup.DiscordGroupGUI;
import com.orbitmines.spigot.servers.hub.gui.friends.FriendGUI;
import com.orbitmines.spigot.servers.hub.gui.stats.StatsGUI;
import com.orbitmines.spigot.servers.hub.handlers.HubDataPointHandler;
import com.orbitmines.spigot.servers.hub.handlers.HubPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Hub extends OrbitMinesServer {

    private World voidWorld;

    private Map<Language, Kit> lobbyKit;

    private List<Location> spawnLocations;

    public static Map<Language, WrittenBookBuilder> RULE_BOOK = new HashMap<>();

    static {
        RULE_BOOK.put(Language.DUTCH, new WrittenBookBuilder(1, "§1 ", "§8§lOrbit§7§lMines", new TextComponent("   §8§lOrbit§7§lMines§4§lRegels" + "\n" + "§0§m-------------------" + "\n" + "§4NIET§0 Adverteren!" + "\n" + "§0Let op je taalgebruik!" + "\n" + "Luister naar de Staff!" + "\n" + "§4GEEN§0 Bugs gebruiken!" + "\n" + "§4NIET§0 hacken!" + "\n" + "§4NIET§0 spammen!" + "\n" + "§4NIET§0 spelers pesten!" + "\n" + "§0\n" + "§0§lVeel Plezier!")));
        RULE_BOOK.put(Language.ENGLISH, new WrittenBookBuilder(1, "§1 ", "§8§lOrbit§7§lMines", new TextComponent("   §8§lOrbit§7§lMines§4§lRules" + "\n" + "§0§m-------------------" + "\n" + "§4DO NOT§0 Advertise!" + "\n" + "§0Watch your Language!" + "\n" + "Listen to Staff!" + "\n" + "§4DO NOT§0 Abuse Bugs!" + "\n" + "§4DO NOT§0 Hack!" + "\n" + "§4DO NOT§0 Spam!" + "\n" + "§4DO NOT§0 Bully Players!" + "\n" + "§0\n" + "§0§lHave Fun!")));
    }

    public Hub(OrbitMines orbitMines) {
        super(orbitMines, Server.HUB, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
    }

    @Override
    public void onEnable() {
        lobbyKit = new HashMap<>();

        preventionSet.prevent(orbitMines.getLobby().getWorld(),
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.MONSTER_EGG_USAGE,
                PreventionSet.Prevention.BUCKET_USAGE,
                PreventionSet.Prevention.CHUNK_UNLOAD,
                PreventionSet.Prevention.CLICK_PLAYER_INVENTORY,
                PreventionSet.Prevention.ENTITY_INTERACTING,
                PreventionSet.Prevention.FOOD_CHANGE,
                PreventionSet.Prevention.ITEM_DROP,
                PreventionSet.Prevention.LEAF_DECAY,
                PreventionSet.Prevention.PLAYER_DAMAGE,
                PreventionSet.Prevention.ITEM_PICKUP,
                PreventionSet.Prevention.SWAP_HAND_ITEMS,
                PreventionSet.Prevention.WEATHER_CHANGE
        );

//        voidWorld = orbitMines.getWorldLoader().loadWorld("VoidWorld", true, WorldLoader.Type.VOID);
//        preventionSet.prevent(voidWorld,
//                PreventionSet.Prevention.BLOCK_BREAK,
//                PreventionSet.Prevention.BLOCK_INTERACTING,
//                PreventionSet.Prevention.BLOCK_PLACE,
//                PreventionSet.Prevention.CHUNK_UNLOAD,
//                PreventionSet.Prevention.CLICK_PLAYER_INVENTORY,
//                PreventionSet.Prevention.ENTITY_INTERACTING,
//                PreventionSet.Prevention.FOOD_CHANGE,
//                PreventionSet.Prevention.ITEM_DROP,
//                PreventionSet.Prevention.LEAF_DECAY,
//                PreventionSet.Prevention.PLAYER_DAMAGE,
//                PreventionSet.Prevention.ITEM_PICKUP,
//                PreventionSet.Prevention.SWAP_HAND_ITEMS,
//                PreventionSet.Prevention.WEATHER_CHANGE
//        );
//        voidWorld.setTime(18000);

        registerKits();

        /* DataPoints */
        spawnLocations = ((HubDataPointSpawnpoint) (orbitMines.getLobby().getHandler().getDataPoint(HubDataPointHandler.Type.SPAWNPOINT))).getSpawns();

        Map<Location, UUID> staffHolos = ((HubDataPointStaffHologram) (orbitMines.getLobby().getHandler().getDataPoint(HubDataPointHandler.Type.STAFF_HOLO))).getStaffHolograms();
        for (Location location : staffHolos.keySet()) {
            UUID uuid = staffHolos.get(location);
            CachedPlayer player = CachedPlayer.getPlayer(uuid);

            String playerName;
            StaffRank staffRank;

            if (player != null) {
                playerName = player.getPlayerName();
                staffRank = player.getStaffRank();
            } else {
                playerName =  "UNKNOWN PLAYER";
                staffRank = StaffRank.NONE;
            }

            Hologram hologram = new Hologram(location, 0, Hologram.Face.UP);
            hologram.addLine(staffRank::getDisplayName, false);
            hologram.addLine(() -> staffRank.getPrefixColor().getChatColor() + playerName, false);
            hologram.create();
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new HubPlayer(this, player);
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
                }
        );
    }

    @Override
    protected void registerCommands() {

    }

    @Override
    protected void registerRunnables() {

    }

    @Override
    public void setupNpc(String npcName, Location location) {

    }

    @Override
    public GameMode getGameMode() {
        return GameMode.ADVENTURE;
    }

    public World getVoidWorld() {
        return voidWorld;
    }

    private void registerKits() {
        for (Language language : Language.values()) {
            KitInteractive kit = new KitInteractive(language.toString());

            {
                kit.setItem(0, RULE_BOOK.get(language));

                new ItemHoverActionBar(new ItemBuilder(Material.WRITTEN_BOOK, 1, "§1 "), false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§4§l" + omp.lang("Regels", "Rules") + "§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.EMERALD, 1, "§2 ");

                kit.setItem(1, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new StatsGUI(omp).open(omp);
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
                PlayerSkullBuilder item = new PlayerSkullBuilder(() -> kit.getLastUsedBy().getPlayerName(), 1, "§3 ");

                kit.setItem(3, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new FriendGUI().open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§b§l" + omp.lang("Vrienden", "Friends") + "§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL, 1, "§4 ");

                kit.setItem(4, new KitInteractive.InteractAction(item) {
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
                ItemBuilder item = new ItemBuilder(Material.REDSTONE_TORCH, 1, "§5 ");

                kit.setItem(5, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        omp.getPlayer().playEffect(omp.getPlayer().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                        new SettingsGUI().open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§c§l" + omp.lang("Instellingen", "Settings") + "§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                PlayerSkullBuilder item = new PlayerSkullBuilder(() -> "Discord Skull", 1, "§6 ").setTexture(DiscordBot.SKULL_TEXTURE);

                kit.setItem(7, new KitInteractive.InteractAction(item) {
                    @Override
                    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                        event.setCancelled(true);

                        new DiscordGroupGUI().open(omp);
                    }
                });

                new ItemHoverActionBar(item, false) {
                    @Override
                    public String getMessage(OMPlayer omp, ItemStack itemStack) {
                        return "§9§lDiscord Squad§r §8- §e§l" + omp.lang("Rechtermuisklik", "Right Click");
                    }
                };
            }

            {
                ItemBuilder item = new ItemBuilder(Material.ENDER_CHEST, 1, "§7 ");

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

            lobbyKit.put(language, kit);

            kit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
        }
    }

    public Kit getLobbyKit(OMPlayer omp) {
        return lobbyKit.get(omp.getLanguage());
    }

    public static class Scoreboard extends DefaultScoreboard {

        public Scoreboard(OrbitMines orbitMines, OMPlayer omp) {
            super(omp,
                    () -> orbitMines.getScoreboardAnimation().get(),
                    () -> "§m--------------",
                    () -> "",
                    () -> "§9§lPrisms",
                    () -> " " + NumberUtils.locale(omp.getPrisms()),
                    () -> " ",
                    () -> "§e§lSolars",
                    () -> " " + NumberUtils.locale(omp.getSolars()) + " ",
                    () -> "  ",
                    () -> "§c§lRank",
                    () -> " " + omp.getRankName(),
                    () -> "   "
            );
        }

        @Override
        public boolean canBypassSettings() {
            return false;
        }
    }
}
