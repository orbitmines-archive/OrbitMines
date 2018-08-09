package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandFly extends VipCommand {

    private String[] alias = { "/fly" };

    public CommandFly() {
        super(Server.SURVIVAL, VipRank.DIAMOND);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (omp.isEligible(StaffRank.MODERATOR) && omp.isOpMode() && a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);
            OMPlayer omp2 = OMPlayer.getPlayer(p2);

            if (p2 != null) {
                if (p2 == p) {
                    p.setAllowFlight(!p.getAllowFlight());
                    p.setFlying(p.getAllowFlight());

                    if (p.isFlying())
                        omp.sendMessage("Fly", Color.LIME, "§7Je §fFly§7 mode staat nu aan.", "§7Your §fFly§7 mode has been enabled.");
                    else
                        omp.sendMessage("Fly", Color.LIME, "§7Je §fFly§7 mode staat nu uit.", "§7Your §fFly§7 mode has been disabled.");

                } else {
                    p2.setAllowFlight(!p2.getAllowFlight());
                    p2.setFlying(p2.getAllowFlight());

                    if (p2.isFlying()) {
                        omp.sendMessage("Fly", Color.LIME, omp2.getName() + "'s §fFly§7 mode staat nu aan.", omp2.getName() + "'s §fFly§7 mode has been enabled.");
                        omp2.sendMessage("Fly", Color.LIME, "§7Je §fFly§7 mode is aangezet door " + omp.getName() + "§7.", omp.getName() + " enabled §7your §fFly§7 mode.");
                    } else {
                        omp.sendMessage("Fly", Color.LIME, omp2.getName() + "'s §fFly§7 mode staat nu uit.", omp2.getName() + "'s §fFly§7 mode has been disabled.");
                        omp2.sendMessage("Fly", Color.LIME, "§7Je §fFly§7 mode is uitgezet door " + omp.getName() + "§7.", omp.getName() + " disabled §7your §fFly§7 mode.");
                    }
                }
            } else {
                omp.sendMessage("Fly", Color.RED, "§7Player §6" + a[1] + " §7is niet online!", "§7Player §6" + a[1] + " §7isn't online!");
            }
        } else if (omp.isEligible(VipRank.DIAMOND) || omp.isEligible(StaffRank.MODERATOR)) {
            p.setAllowFlight(!p.getAllowFlight());
            p.setFlying(p.getAllowFlight());

            if (p.isFlying())
                omp.sendMessage("Fly", Color.LIME, "§7Je §fFly§7 mode staat nu aan.", "§7Your §fFly§7 mode has been enabled.");
            else
                omp.sendMessage("Fly", Color.LIME, "§7Je §fFly§7 mode staat nu uit.", "§7Your §fFly§7 mode has been disabled.");
        } else {
            omp.sendMessage(Message.REQUIRE_RANK(VipRank.DIAMOND));
        }
    }
}
