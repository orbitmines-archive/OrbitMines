package com.orbitmines.bungeecord.events;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.StaffRank;
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

        //TODO REMOVE, ONLY TEMPORARY

        if (event.getConnection().getName().equals("Rush_matthias"))
            return;

        CachedPlayer player = CachedPlayer.getPlayer(event.getConnection().getName());
        if (player == null || (player.getStaffRank() == StaffRank.NONE || player.getStaffRank() == StaffRank.BUILDER)) {
            event.setCancelled(true);
            event.setCancelReason("§8§lOrbit§7§lMines\n§a§lWe're coming back soon!\n\n§7For more updates be sure to join us on:\n§9§lDiscord§r §7» §9https://discord.gg/QjVGJMe\n§6§lWebsite§r §7» §6https://www.orbitmines.com");
            return;
        }

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

        if (omp == null)
            return;

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

        if (omp == null)
            return;

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
