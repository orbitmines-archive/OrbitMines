package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Message;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import com.orbitmines.bungeecord.handlers.cmd.VipCommand;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandHelp extends Command {

    private String[] alias = { "/help" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return null;
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        omp.sendMessage("");
        omp.sendMessage(" §8§lOrbit§7§lMines §9§lCommand Help");

        for (Command command : getCommands()) {
            if (command instanceof CommandHelp || command instanceof StaffCommand && !omp.isEligible(((StaffCommand) command).getStaffRank()))
                continue;

            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message("  §7- "));

            if (command instanceof VipCommand) {
                cM.add(new Message(((VipCommand) command).getVipRank().getDisplayName() + "§r"));
                cM.add(new Message(" "));
            } else if (command instanceof StaffCommand) {
                cM.add(new Message(((StaffCommand) command).getStaffRank().getDisplayName() + "§r"));
                cM.add(new Message(" "));
            }

            command.getHelpMessage(omp, cM).send(omp);
        }

        /* Command continues on Bukkit Side */
        event.setCancelled(false);
    }
}
