package com.orbitmines.bungeecord.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class VipCommand extends Command {

    private final VipRank vipRank;

    public VipCommand(VipRank vipRank) {
        this.vipRank = vipRank;
    }

    public abstract void onDispatch(ChatEvent event, BungeePlayer omp, String[] a);

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (omp.isEligible(vipRank))
            onDispatch(event, omp, a);
        else
            omp.sendMessage(Message.REQUIRE_RANK(vipRank));
    }

    public VipRank getVipRank() {
        return vipRank;
    }
}
