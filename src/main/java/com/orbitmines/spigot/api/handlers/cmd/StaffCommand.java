package com.orbitmines.spigot.api.handlers.cmd;

import com.madblock.api.Rank;
import com.madblock.api.utils.Message;
import com.madblock.spigot.api.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class StaffCommand extends Command {

    private final Rank rank;

    public StaffCommand(Rank rank) {
        this.rank = rank;
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (mbp.isEligible(rank))
            onDispatch(mbp, a);
        else
            mbp.sendMessage(Message.UNKNOWN_COMMAND);
    }

    public Rank getRank() {
        return rank;
    }
}
