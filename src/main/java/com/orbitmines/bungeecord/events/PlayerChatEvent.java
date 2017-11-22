package com.orbitmines.bungeecord.events;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayerChatEvent implements Listener {

    private OrbitMinesBungee bungee;

    public PlayerChatEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        BungeePlayer mbp = BungeePlayer.getPlayer(player);
        String[] a = event.getMessage().split(" ");

        if (!a[0].startsWith("/")) {
            /* Message is not a command */

            if (a[0].startsWith("@") && mbp.isEligible(StaffRank.MODERATOR)) {
                /* Staff Message */
                event.setCancelled(true);

                if (!mbp.isLoggedIn()) {
                    mbp.sendMessage(Message.ENTER_2FA);
                    return;
                }

                if (event.getMessage().length() != 1) {
                    bungee.broadcast(StaffRank.MODERATOR, "Staff", Color.AQUA, mbp.getStaffRank().getPrefix() + mbp.getName() + " §7» §f§l" + event.getMessage().substring(1));
                } else {
                    mbp.sendMessage("Staff", Color.RED, "§7Use §6@<message>§7.");
                }
            }
        } else if (!mbp.isLoggedIn()) {
            event.setCancelled(true);
            mbp.sendMessage(Message.ENTER_2FA);
        } else {
            /* Message is a command */
            Command command = Command.getCommand(a[0]);

            if (command == null)
                return;

            event.setCancelled(true);
            command.dispatch(event, mbp, a);
        }
    }
}
