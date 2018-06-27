package com.orbitmines.spigot.servers.survival.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class FlyEvent implements Listener {

    private Survival survival;

    private Map<SurvivalPlayer, BukkitTask> noFall;

    public FlyEvent(Survival survival) {
        this.survival = survival;
        this.noFall = new HashMap<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        /* OpMode can fly anywhere */
        if (omp.isOpMode())
            return;

        if (p.getWorld() == survival.getWorld()) {
            Claim claim = survival.getClaimHandler().getClaimAt(p.getLocation(), false, omp.getLastClaim());
            if (claim != null)
                omp.setLastClaim(claim);

            if (claim == null || !claim.canAccess(omp)) {
                if (p.isFlying()) {
                    disableFly(omp, true);

                    new ActionBar(omp, () -> omp.lang("§c§lJe kan alleen vliegen in je claims, of claims waar je §a§l" + Claim.Permission.ACCESS.getName().lang(omp.getLanguage()) + "§c§l hebt.", "§c§lYou are only allowed to fly in your claims, and claims where you have §a§l" + Claim.Permission.ACCESS.getName().lang(omp.getLanguage()) + "§c§l."), 60).send();
                }
            } else if (noFall.containsKey(omp)) {
                p.setAllowFlight(true);
                p.setFlying(true);

                noFall.get(omp).cancel();
                noFall.remove(omp);
            }

        } else if (p.isFlying()) {
            if (p.getWorld() == survival.getWorld_nether()) {
                disableFly(omp, false);
                notAllowedToFly(omp, "§c§lNether");
            } else if (p.getWorld() == survival.getWorld_the_end()) {
                disableFly(omp, false);
                notAllowedToFly(omp, "§7§lEnd");
            }
        }
    }

    private void notAllowedToFly(SurvivalPlayer omp, String world) {
        new ActionBar(omp, () -> omp.lang(new Message("§c§lHet is niet toegestaan om in de " + world + "§c§l te vliegen.", "§c§lYou are not allowed to fly in the " + world + "§c§l.")), 60).send();
    }

    private void disableFly(SurvivalPlayer omp, boolean noFall) {
        omp.getPlayer().setFlying(false);
        omp.getPlayer().setAllowFlight(false);

        if (!noFall)
            return;

        this.noFall.put(omp, new BukkitRunnable() {
            @Override
            public void run() {
                FlyEvent.this.noFall.remove(omp);
            }
        }.runTaskLater(survival.getOrbitMines(), 150));
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;

        Player p = (Player) event.getEntity();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (!noFall.containsKey(omp))
            return;

        event.setCancelled(true);
        noFall.get(omp).cancel();
        noFall.remove(omp);
    }
}
