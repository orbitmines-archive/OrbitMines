package com.orbitmines.bungeecord.events;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.punishment.Punishment;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.discordbot.utils.SkinLibrary;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class JoinQuitEvents implements Listener {

    private final OrbitMinesBungee bungee;

    public JoinQuitEvents(OrbitMinesBungee bungee) {
        this.bungee = bungee;
    }

    @EventHandler
    public void onLogin(PreLoginEvent event) {
        /* Setup Discord Emote */
        SkinLibrary.setupEmote(bungee.getDiscord().getGuild(bungee.getToken()), event.getConnection().getName());

        CachedPlayer player = CachedPlayer.getPlayer(event.getConnection().getName());
        if (player == null)
            return;

        PunishmentHandler handler = PunishmentHandler.getHandler(player.getUUID());
        Punishment active = handler.getActivePunishment(Punishment.Type.BAN);

        /* Check if player is banned */
        if (active != null) {
            event.setCancelled(true);
            event.setCancelReason(active.getBanString(player.getLanguage()));
            return;
        }
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        /* Otherwise login */
        BungeePlayer.getPlayer(event.getPlayer());
    }

    @EventHandler
    public void onConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        /* null when not switching server */
        if (player.getServer() != null)
            return;

        BungeePlayer omp = BungeePlayer.getPlayer(player);

        ServerInfo fallBackServer = omp.getFallBackServer();

        if (fallBackServer != null) {
            event.setTarget(fallBackServer);
            return;
        }

        player.disconnect(omp.lang("§8§lOrbit§7§lMines\n§7Kan geen server vinden om mee te verbinden.", "§8§lOrbit§7§lMines\n§7Cannot find a server to fall back to."));
    }

    @EventHandler
    public void onKick(ServerKickEvent event) {
        BungeePlayer omp = BungeePlayer.getPlayer(event.getPlayer());

        ServerInfo fallBackServer = omp.getFallBackServer();

        if (fallBackServer == null) {
            omp.getPlayer().disconnect(omp.lang("§8§lOrbit§7§lMines\n§7Kan geen server vinden om mee te verbinden.", "§8§lOrbit§7§lMines\n§7Cannot find a server to fall back to."));
            return;
        }

        event.setCancelled(true);
        event.setCancelServer(fallBackServer);
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BungeePlayer omp = BungeePlayer.getPlayer(player);
        omp.logout();
    }
}
