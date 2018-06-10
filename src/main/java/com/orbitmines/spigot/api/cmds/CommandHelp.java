package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;

public class CommandHelp extends Command {

    private String[] alias = { "/help" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        omp.sendMessage("");
        omp.sendMessage("");
        omp.sendMessage(" §7§lOrbit§8§lMines §9§lCommand Help");

        for (Command command : getCommands()) {
            if (command instanceof CommandHelp || command instanceof StaffCommand && !omp.isEligible(((StaffCommand) command).getStaffRank()))
                continue;

            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message("  §7- "));
            command.getHelpMessage(omp, cM).send(omp);
        }
    }
}
