package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandFly extends VipCommand {

    private final Survival survival;

    public CommandFly(Survival survival) {
        super(CommandLibrary.SURVIVAL_FLY);

        this.survival = survival;
    }

    @Override
    public void onDispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
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
        } else if (omp.isEligible(VipRank.DIAMOND)) {
            if (!omp.isOpMode()) {
                Claim claim = survival.getClaimHandler().getClaimAt(p.getLocation(), false, omp.getLastClaim());
                if (claim != null)
                    omp.setLastClaim(claim);

                if (claim == null || !claim.canAccess(omp, false)) {
                    omp.sendMessage("Fly", Color.RED, "Je kan alleen vliegen in je claims, of claims waar je §a§l" + Claim.Permission.ACCESS.getName().lang(omp.getLanguage()) + "§7 hebt.", "You are only allowed to fly in your claims, and claims where you have §a§l" + Claim.Permission.ACCESS.getName().lang(omp.getLanguage()) + "§7.");
                    return;
                }
            }

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
