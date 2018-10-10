package com.orbitmines.bungeecord.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class StaffCommand extends Command {

    public StaffCommand(CommandLibrary library) {
        super(library);

        if (library.getStaffRank() == null)
            throw new IllegalStateException();
    }

    public abstract void onDispatch(ChatEvent event, BungeePlayer omp, String[] a);

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (omp.isEligible(getStaffRank()))
            onDispatch(event, omp, a);
        else
            omp.sendMessage(Message.UNKNOWN_COMMAND);
    }

    public StaffRank getStaffRank() {
        return library.getStaffRank();
    }
}
