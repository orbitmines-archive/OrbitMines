package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandFind extends StaffCommand {

    private OrbitMinesBungee bungee;

    public CommandFind(OrbitMinesBungee bungee) {
        super(CommandLibrary.FIND);

        this.bungee = bungee;
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length < 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        BungeePlayer player = BungeePlayer.getPlayer(a[1]);

        if (player == null) {
            omp.sendMessage("Server", Color.RED, "§7Die speler is niet online.", "§7That player is not online!");
            return;
        }

        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message("Server", Color.BLUE, player.getName() + "§7 is momenteel in ", player.getName() + "§7 is currently on "));

        Server server = player.getServer();

        cM.add(new Message(server.getDisplayName()), ClickEvent.Action.RUN_COMMAND, new Message("/server " + server.getName()), HoverEvent.Action.SHOW_TEXT, new Message("§7Klik hier om te verbinden met " + server.getDisplayName() + "§7.", "§7Click here to connect to " + server.getDisplayName() + "§7."));
        cM.add(new Message("§7."));

        cM.send(omp);
    }
}
