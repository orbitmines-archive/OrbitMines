package com.orbitmines.bungeecord.events;

import com.orbitmines.bungeecord.OrbitMinesBungee;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PingEvent implements Listener {

    private OrbitMinesBungee bungee;

    public PingEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();
        serverPing.setDescriptionComponent(new TextComponent(bungee.getMotdHandler().getMessage()));
        serverPing.getPlayers().setMax(bungee.getMaxPlayers());
    }
}
