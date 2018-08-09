package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class StaffCommand extends Command {

    private final StaffRank staffRank;

    public StaffCommand(StaffRank rank) {
        this(null, rank);
    }

    public StaffCommand(Server server, StaffRank rank) {
        super(server);

        this.staffRank = rank;
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.isEligible(staffRank))
            onDispatch(omp, a);
        else
            omp.sendMessage(Message.UNKNOWN_COMMAND);
    }

    public StaffRank getStaffRank() {
        return staffRank;
    }
}
