package com.orbitmines.spigot.api.handlers.cmd;

import com.madblock.api.VipRank;
import com.madblock.spigot.api.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class VipCommand extends Command {

    private final VipRank vipRank;

    public VipCommand(VipRank vipRank) {
        this.vipRank = vipRank;
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (mbp.isEligible(vipRank))
            onDispatch(mbp, a);
        else
            mbp.sendMessage("§7Access denied, you do not own the following rank: " + vipRank.getPrefix() + "§7.");
    }

    public VipRank getVipRank() {
        return vipRank;
    }
}
