package com.orbitmines.bungeecord.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class StaffCommand extends Command {

    private final StaffRank staffRank;

    public StaffCommand(StaffRank staffRank) {
        this.staffRank = staffRank;
    }

    public abstract void onDispatch(ChatEvent event, BungeePlayer omp, String[] a);

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (omp.isEligible(staffRank))
            onDispatch(event, omp, a);
        else
            omp.sendMessage(Message.UNKNOWN_COMMAND);
    }

    public StaffRank getStaffRank() {
        return staffRank;
    }
}
