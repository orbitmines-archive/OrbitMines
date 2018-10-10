package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class VipCommand extends Command {

    public VipCommand(CommandLibrary library) {
        super(library);

        if (library.getVipRank() == null)
            throw new IllegalStateException();
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.isEligible(getVipRank()))
            onDispatch(omp, a);
        else
            omp.sendMessage(Message.REQUIRE_RANK(getVipRank()));
    }

    public VipRank getVipRank() {
        return library.getVipRank();
    }
}
