package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.discordbot.utils.DiscordBungeeUtils;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandHelp extends Command {

    public CommandHelp() {
        super(CommandLibrary.HELP);
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        omp.sendMessage("");
        omp.sendMessage(" §8§lOrbit§7§lMines §9§lCommand Help");

        Server server = omp.getServer();
        List<CommandLibrary> library = CommandLibrary.getLibraryAll(server);

        List<CommandLibrary> thisServer = new ArrayList<>();
        for (CommandLibrary command : library) {
            if (command.getServer() == server)
                thisServer.add(command);
        }

        List<CommandLibrary> global = new ArrayList<>(library);
        global.removeAll(thisServer);

        send(omp, global);

        if (thisServer.size() == 0)
            return;

        omp.sendMessage(" " + server.getDisplayName());
        send(omp, thisServer);
    }

    private void send(BungeePlayer omp, List<CommandLibrary> library) {
        for (CommandLibrary cmd : library) {
            if (cmd.isStaffCommand() && !omp.isEligible(cmd.getStaffRank()))
                continue;

            ComponentMessage cM = new ComponentMessage();
            cM.add("  §7- ");

            if (cmd.isStaffCommand())
                cM.add(DiscordBungeeUtils.getCommandMention(omp, cmd, cmd.getStaffRank().getDisplayName() + "§r ").setChatColor(omp.getStaffRank().getPrefixColor().getMd5()));
            else if (cmd.isVipCommand())
                cM.add(DiscordBungeeUtils.getCommandMention(omp, cmd, cmd.getVipRank().getDisplayName() + "§r ").setChatColor(omp.getVipRank().getPrefixColor().getMd5()));

            cM.add(DiscordBungeeUtils.getCommandMention(omp, cmd, cmd.getAlias()[0]).setChatColor(Color.BLUE.getMd5()));

            String argsHelp = cmd.getArgsHelp(omp.getStaffRank(), omp.getVipRank());
            if (argsHelp != null)
                cM.add(DiscordBungeeUtils.getCommandMention(omp, cmd, " " + argsHelp).setChatColor(Color.SILVER.getMd5()));

            cM.send(omp);
        }
    }
}
