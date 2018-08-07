package com.orbitmines.spigot.api.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;

public class CommandNick extends VipCommand {

    private String[] alias = { "/nick" };

    public CommandNick() {
        super(VipRank.GOLD);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "<name>|off";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (a.length == 2) {
            if (a[1].length() <= 30) {

                if (a[1].equalsIgnoreCase("off")) {
                    omp.sendMessage("Nick", Color.LIME, "§7Je §6nickname§7 is verwijderd!", "§7Removed your §6nickname§7!");
                    omp.clearNickName();
                } else {
                    for (int i = 0; i < a[1].length(); i++) {
                        int c = a[1].charAt(i);
                        if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_') {
                            omp.sendMessage("Nick", Color.RED, "§7Alleen §6letters§7, §6cijfers§7 en '§6_§7' zijn toegestaan!", "§7Only §6alphabetic§7, §6numeric§7 and '§6_§7' characters are allowed!");
                            return;
                        }
                    }

                    omp.sendMessage("Nick", Color.LIME, "§7Je §6nickname§7 is veranderd in '§a" + a[1] + "§7'.", "§7Changed your §6nickname§7 to '§a" + a[1] + "§7'.");
                    omp.setNickName(a[1]);
                }
            } else {
                omp.sendMessage("Nick", Color.RED, "§7Je §6nickname§7 kan niet langer dan §630 karakters§7 zijn!", "§7Your §6nickname§7 cannot be longer than §630 characters§7!");
            }
        } else {
            getHelpMessage(omp).send(omp);
        }
    }
}
