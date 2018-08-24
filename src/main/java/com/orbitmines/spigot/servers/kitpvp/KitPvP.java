package com.orbitmines.spigot.servers.kitpvp;

import com.orbitmines.api.Server;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPMap;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitPvP extends OrbitMinesServer {

    public KitPvP(OrbitMines orbitMines, Server server, PluginMessageHandler messageHandler) {
        super(orbitMines, server, messageHandler);
    }

    @Override
    public void onEnable() {
        preventionSet.prevent(orbitMines.getLobby().getWorld(),
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.CHUNK_UNLOAD,
                PreventionSet.Prevention.ENTITY_INTERACTING,
                PreventionSet.Prevention.LEAF_DECAY,
                PreventionSet.Prevention.PLAYER_DAMAGE,
                PreventionSet.Prevention.WEATHER_CHANGE,
                PreventionSet.Prevention.MONSTER_EGG_USAGE
        );

        /* Load & Create all Maps from Database */
        KitPvPMap.loadMaps();

        for (KitPvPMap map : KitPvPMap.getMaps()) {
            preventionSet.prevent(map.getWorld(),
                    PreventionSet.Prevention.BLOCK_BREAK,
                    PreventionSet.Prevention.BLOCK_PLACE,
                    PreventionSet.Prevention.LEAF_DECAY,
                    PreventionSet.Prevention.WEATHER_CHANGE,

                    PreventionSet.Prevention.FOOD_CHANGE,
                    PreventionSet.Prevention.BLOCK_SPREAD,
                    PreventionSet.Prevention.EXPLOSION_DAMAGE,
                    PreventionSet.Prevention.PHYSICAL_INTERACTING_EXCEPT_PLATES
            );
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new KitPvPPlayer(player);
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        return true;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return null;
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.SURVIVAL;
    }

    @Override
    public void format(AsyncPlayerChatEvent event, OMPlayer player) {
        super.format(event, player);

        KitPvPPlayer omp = (KitPvPPlayer) player;

        /* Add level in front of chat */
        event.setFormat(omp.getLevelData().getPrefix() + (omp.getPlayer().getGameMode() == GameMode.SPECTATOR ? " §eSpec " : "") + event.getFormat());
    }

    @Override
    protected void registerEvents() {
        //TODO
    }

    @Override
    protected void registerCommands() {
        //TODO
    }

    @Override
    protected void registerRunnables() {
        //TODO
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
                    () -> "§6§lCoins",
                    () -> " " + NumberUtils.locale(omp.getCoins()),
                    () -> " ",
                    () -> "§c§lKills",
                    () -> " " + NumberUtils.locale(omp.getKills()) + " ",
                    () -> "  ",
                    () -> "§4§lDeaths",
                    () -> " " + NumberUtils.locale(omp.getDeaths()) + "  ",
                    () -> "   "

            );
        }

        @Override
        public boolean canBypassSettings() {
            return false;
        }
    }
}
