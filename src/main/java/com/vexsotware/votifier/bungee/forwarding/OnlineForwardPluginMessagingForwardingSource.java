package com.vexsotware.votifier.bungee.forwarding;

import com.madblock.bungeecord.MadBlockBungee;
import com.vexsoftware.votifier.bungee.forwarding.cache.VoteCache;
import com.vexsoftware.votifier.model.Vote;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class OnlineForwardPluginMessagingForwardingSource extends AbstractPluginMessagingForwardingSource implements Listener {

    private final String fallbackServer;

    public OnlineForwardPluginMessagingForwardingSource(String channel, MadBlockBungee nuVotifier, VoteCache cache, String fallbackServer) {
        super(channel, nuVotifier, cache);
        this.fallbackServer = fallbackServer;
        ProxyServer.getInstance().getPluginManager().registerListener(nuVotifier, this);
    }

    public void forward(Vote v) {
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(v.getUsername());

        if ((p == null) || (!forwardSpecific(p.getServer().getInfo(), v))) {
            ServerInfo serverInfo = ProxyServer.getInstance().getServers().get(fallbackServer);

            if ((serverInfo != null) && (!forwardSpecific(serverInfo, v))) {
                attemptToAddToCache(v, fallbackServer);
            }
        }
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equals(channel))
            e.setCancelled(true);
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent e) {
        handleServerConnected(e);
    }
}
