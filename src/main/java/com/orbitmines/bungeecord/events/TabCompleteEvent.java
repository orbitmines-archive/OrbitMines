package com.orbitmines.bungeecord.events;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TabCompleteEvent implements Listener {

    @EventHandler
    public void onTabComplete(net.md_5.bungee.api.event.TabCompleteEvent event) {
        if (!event.getSuggestions().isEmpty() || !(event.getSender() instanceof ProxiedPlayer))
            return;

        String[] a = event.getCursor().split(" ");

        if (a.length == 0)
            return;

        BungeePlayer omp = BungeePlayer.getPlayer((ProxiedPlayer) event.getSender());

        if (a.length > 1) {
            String checked = a[a.length - 1];

            for (BungeePlayer player : BungeePlayer.getPlayers(omp.getServer())) {
                if (player.getName().startsWith(checked)) {
                    if (omp.isEligible(StaffRank.MODERATOR) || !player.isSilent())
                        event.getSuggestions().add(player.getName());
                }
            }
        } else if(event.getCursor().substring(a[0].length()).startsWith(" ")){
            for (BungeePlayer player : BungeePlayer.getPlayers(omp.getServer())) {
                if (omp.isEligible(StaffRank.MODERATOR) || !player.isSilent())
                    event.getSuggestions().add(player.getName());
            }
        }
    }
}
