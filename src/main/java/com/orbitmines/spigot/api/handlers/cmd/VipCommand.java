package com.orbitmines.spigot.api.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class VipCommand extends Command {

    private final VipRank vipRank;

    public VipCommand(Server server, VipRank vipRank) {
        super(server);
        
        this.vipRank = vipRank;
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.isEligible(vipRank))
            onDispatch(omp, a);
        else
            omp.sendMessage(Message.REQUIRE_RANK(vipRank));
    }

    public VipRank getVipRank() {
        return vipRank;
    }
}
