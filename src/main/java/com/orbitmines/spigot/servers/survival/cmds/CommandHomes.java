package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class CommandHomes extends Command {

    private String[] alias = { "/homes" };

    public CommandHomes() {
        super(Server.SURVIVAL);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (omp.getHomes().size() == 0) {
            omp.sendMessage("Home", Color.RED, "§7Je hebt nog geen " + Home.COLOR.getChatColor() + "home§7 neergezet!", "§7You haven't set a " + Home.COLOR.getChatColor() + "home§7 yet!");
            return;
        }

        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message(Message.FORMAT("Home", Color.BLUE, omp.lang("§7Jouw Homes: ", "§7Your Homes: "))));

        for (int i = 0; i < omp.getHomes().size(); i++) {
            if (i != 0)
                cM.add(new Message("§7, "));

            Home home = omp.getHomes().get(i);
            cM.add(new Message(Home.COLOR.getChatColor() + home.getName()), ClickEvent.Action.RUN_COMMAND, new Message("/home " + home.getName()), HoverEvent.Action.SHOW_TEXT, new Message("§7Klik hier om te teleporteren naar " + Home.COLOR.getChatColor() + home.getName() + "§7.", "§7Click here to teleport to " + Home.COLOR.getChatColor() + home.getName() + "§7."));
        }

        cM.send(omp);
    }
}
