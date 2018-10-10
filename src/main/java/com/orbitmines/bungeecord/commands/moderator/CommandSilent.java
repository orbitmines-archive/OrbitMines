package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandSilent extends StaffCommand {

    public CommandSilent() {
        super(CommandLibrary.SILENT);
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        omp.setSilent(!omp.isSilent());

        if (omp.isSilent())
            omp.sendMessage("Silent Mode", Color.LIME, "§6Silent Mode §7geactiveerd.", "§6Silent Mode §7activated.");
        else
            omp.sendMessage("Silent Mode", Color.RED, "§6Silent Mode §7gedeactiveerd.", "§6Silent Mode §7deactivated.");
    }
}
