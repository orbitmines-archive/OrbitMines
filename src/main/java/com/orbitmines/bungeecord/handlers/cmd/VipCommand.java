package com.orbitmines.bungeecord.handlers.cmd;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class VipCommand extends Command {

    public VipCommand(CommandLibrary library) {
        super(library);

        if (library.getVipRank() == null)
            throw new IllegalStateException();
    }
    public abstract void onDispatch(ChatEvent event, BungeePlayer omp, String[] a);

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (omp.isEligible(getVipRank()))
            onDispatch(event, omp, a);
        else
            omp.sendMessage(Message.REQUIRE_RANK(getVipRank()));
    }

    public VipRank getVipRank() {
        return library.getVipRank();
    }
}
