package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.teleportable.Home;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.List;

public class CommandHomes extends Command {

    public CommandHomes() {
        super(CommandLibrary.SURVIVAL_HOMES);
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (a.length != 1 && omp.isEligible(StaffRank.ADMIN)) {
            CachedPlayer cachedPlayer = CachedPlayer.getPlayer(a[1]);

            if (cachedPlayer == null) {
                omp.sendMessage("Claim", "§7Kan die speler niet vinden.", "§7That player cannot be found.");
                return;
            }

            SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(cachedPlayer.getUUID());

            String playerName;
            List<Home> homes;
            if (omp2 == null) {
                playerName = cachedPlayer.getRankPrefixColor().getChatColor() + cachedPlayer.getPlayerName();
                homes = Home.getHomesFor(cachedPlayer.getUUID());
            } else {
                playerName = omp2.getName();
                homes = omp2.getHomes();
            }

            if (homes.size() == 0) {
                omp.sendMessage("Home", Color.RED, playerName + "§7 heeft nog geen " + Home.COLOR.getChatColor() + "home§7 neergezet!", playerName + "§7 hasn't set a " + Home.COLOR.getChatColor() + "home§7 yet!");
                return;
            }

            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message(Message.FORMAT("Home", Color.BLUE, omp.lang(playerName + "§7's Homes", playerName + "§7's Homes") + ": ")));

            for (int i = 0; i < homes.size(); i++) {
                if (i != 0)
                    cM.add(new Message("§7, "));

                Home home = homes.get(i);
                cM.add(new Message(Home.COLOR.getChatColor() + home.getName()), ClickEvent.Action.RUN_COMMAND, new Message("/teleport " + omp.getName(true) + " " + home.getLocation().getBlockX() + " " + home.getLocation().getBlockY() + " " + home.getLocation().getBlockZ()), HoverEvent.Action.SHOW_TEXT, new Message("§7Klik hier om te teleporteren naar " + Home.COLOR.getChatColor() + home.getName() + "§7.", "§7Click here to teleport to " + Home.COLOR.getChatColor() + home.getName() + "§7."));
            }

            cM.send(omp);
        } else {
            if (omp.getHomes().size() == 0) {
                omp.sendMessage("Home", Color.RED, "§7Je hebt nog geen " + Home.COLOR.getChatColor() + "home§7 neergezet!", "§7You haven't set a " + Home.COLOR.getChatColor() + "home§7 yet!");
                return;
            }

            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message(Message.FORMAT("Home", Color.BLUE, omp.lang("§7Jouw Homes", "§7Your Homes") + " (§6" + omp.getHomes().size() + "§7 / " + omp.getHomesAllowed() + "): ")));

            for (int i = 0; i < omp.getHomes().size(); i++) {
                if (i != 0)
                    cM.add(new Message("§7, "));

                Home home = omp.getHomes().get(i);
                cM.add(new Message(Home.COLOR.getChatColor() + home.getName()), ClickEvent.Action.RUN_COMMAND, new Message("/home " + home.getName()), HoverEvent.Action.SHOW_TEXT, new Message("§7Klik hier om te teleporteren naar " + Home.COLOR.getChatColor() + home.getName() + "§7.", "§7Click here to teleport to " + Home.COLOR.getChatColor() + home.getName() + "§7."));
            }

            cM.send(omp);
        }
    }
}
