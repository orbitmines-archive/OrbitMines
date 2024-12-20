package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class StaffCommand extends Command {

    public StaffCommand(CommandLibrary library) {
        super(library);

        if (library.getStaffRank() == null)
            throw new IllegalStateException();
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.isEligible(getStaffRank()))
            onDispatch(omp, a);
        else
            omp.sendMessage(Message.UNKNOWN_COMMAND);
    }

    public StaffRank getStaffRank() {
        return library.getStaffRank();
    }
}
