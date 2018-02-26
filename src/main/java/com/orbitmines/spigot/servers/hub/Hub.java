package com.orbitmines.spigot.servers.hub;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.events.VoidDamageEvent;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.handlers.kit.KitInteractive;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointSpawnpoint;
import com.orbitmines.spigot.servers.hub.handlers.HubDataPointHandler;
import com.orbitmines.spigot.servers.hub.handlers.HubPlayer;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Hub extends OrbitMinesServer {

    private World voidWorld;

    private Map<Language, Kit> lobbyKit;

    private List<Location> spawnLocations;

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

        voidWorld = orbitMines.getWorldLoader().loadWorld("VoidWorld", true, WorldLoader.Type.VOID);
        preventionSet.prevent(voidWorld,
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
                PreventionSet.Prevention.WEATHER_CHANGE
        );
        voidWorld.setTime(18000);

        registerKits();

        /* DataPoints */
        spawnLocations = ((HubDataPointSpawnpoint) (orbitMines.getLobby().getHandler().getDataPoint(HubDataPointHandler.Type.SPAWNPOINT))).getSpawns();
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
    protected void registerEvents() {
        registerEvents(
                new VoidDamageEvent() {
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

    public World getVoidWorld() {
        return voidWorld;
    }

    private void registerKits() {
        for (Language language : Language.values()) {
            KitInteractive kit = new KitInteractive(language.toString());

            String rules;
            switch (language) {

                case DUTCH:
                    rules = "   §8§lOrbit§7§lMines§4§lRegels" + "\n" + "§0§m-------------------" + "\n" + "§4NIET§0 Adverteren!" + "\n" + "§0Let op je taalgebruik!" + "\n" + "Luister naar de Staff!" + "\n" + "§4GEEN§0 Bugs gebruiken!" + "\n" + "§4NIET§0 hacken!" + "\n" + "§4NIET§0 spammen!" + "\n" + "§4NIET§0 spelers pesten!" + "\n" + "§0\n" + "§0§lVeel Plezier!";
                    break;
                case ENGLISH:
                    rules = "   §8§lOrbit§7§lMines§4§lRules" + "\n" + "§0§m-------------------" + "\n" + "§4DO NOT§0 Advertise!" + "\n" + "§0Watch your Language!" + "\n" + "Listen to Staff!" + "\n" + "§4DO NOT§0 Abuse Bugs!" + "\n" + "§4DO NOT§0 Hack!" + "\n" + "§4DO NOT§0 Spam!" + "\n" + "§4DO NOT§0 Bully Players!" + "\n" + "§0\n" + "§0§lHave Fun!";
                    break;
                default:
                    return;
            }

            kit.setItem(0, new WrittenBookBuilder(1, new Message("§4§nServer Regels", "§4§nServer Rules").lang(language), "§8§lOrbit§7§lMines", rules).build());

            kit.setItem(3, new KitInteractive.InteractAction(new ItemBuilder(Material.EXP_BOTTLE, 1, 0, "§d§nAchievements").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);

                    //TODO OPEN ACHIEVEMENTS
                }
            });

            kit.setItem(4, new KitInteractive.InteractAction(new ItemBuilder(Material.ENDER_PEARL, 1, 0, "§3§nServer Selector").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);

                    //TODO OPEN SERVER SELECTION0
                }
            });

            kit.setItem(5, new KitInteractive.InteractAction(new ItemBuilder(Material.REDSTONE_TORCH_ON, 1, 0, new Message("§c§nInstellingen", "§c§nSettings").lang(language)).build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);

                    omp.getPlayer().playEffect(omp.getPlayer().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                    //TODO OPEN SETTINGS
                }
            });

            kit.setItem(8, new KitInteractive.InteractAction(new ItemBuilder(Material.ENDER_CHEST, 1, 0, "§9§nCosmetic Perks").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);

//                    if (omp.canReceiveVelocity())
//                        TODO OPEN COSMETICS
                }
            });

            lobbyKit.put(language, kit);
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
    }
}
