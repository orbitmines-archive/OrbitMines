package com.orbitmines.spigot.api.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;

public class CommandAfk extends VipCommand {

    private String[] alias = { "/afk" };

    public CommandAfk() {
        super(VipRank.IRON);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "(reason)";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (omp.isAfk()) {
            omp.noLongerAfk();
        } else {
            if (a.length == 1) {
                omp.setAfk("null");
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < a.length; i++) {
                if (i != 1)
                    stringBuilder.append(" ");

                stringBuilder.append(a[i]);
            }
            String message = stringBuilder.toString();

            omp.setAfk(message);
        }
    }
}
