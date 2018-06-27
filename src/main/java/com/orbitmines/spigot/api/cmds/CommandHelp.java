package com.orbitmines.spigot.api.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp extends Command {

    private String[] alias = { "/help" };

    private final OrbitMines orbitMines;

    public CommandHelp(OrbitMines orbitMines) {
        super(null);

        this.orbitMines = orbitMines;
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
    public void dispatch(OMPlayer omp, String[] a) {
//        omp.sendMessage("");
//        omp.sendMessage(" §7§lOrbit§8§lMines §9§lCommand Help");

        List<Command> thisServer = new ArrayList<>();
        for (Command command : getCommands()) {
            if (command.getServer() == orbitMines.getServerHandler().getServer())
                thisServer.add(command);
        }

        List<Command> global = new ArrayList<>(getCommands());
        global.removeAll(thisServer);

        send(omp, global);

        if (thisServer.size() == 0)
            return;

        omp.sendMessage(" " + orbitMines.getServerHandler().getServer().getDisplayName());
        send(omp, thisServer);
    }

    private void send(OMPlayer omp, List<Command> commands) {
        for (Command command : commands) {
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
    }
}
