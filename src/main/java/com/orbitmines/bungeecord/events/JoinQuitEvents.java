package com.orbitmines.bungeecord.events;

import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class JoinQuitEvents implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        //TODO CHECK IF BANNED

        /* Otherwise login */
        BungeePlayer bp = new BungeePlayer(event.getPlayer());
        bp.login();
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
    public void onQuit(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.getServer().getInfo() == event.getTarget())
            BungeePlayer.getPlayer(player).logout();//TODO AFTER CRASH = NULL?
    }
}
