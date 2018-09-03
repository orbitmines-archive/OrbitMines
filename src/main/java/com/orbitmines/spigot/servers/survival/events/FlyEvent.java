package com.orbitmines.spigot.servers.survival.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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
    private Map<SurvivalPlayer, Timer> timers;

    public FlyEvent(Survival survival) {
        this.survival = survival;
        this.noFall = new HashMap<>();
        this.timers = new HashMap<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        /* OpMode can fly anywhere */
        if (omp.isOpMode() || omp.getGameMode() != GameMode.SURVIVAL)
            return;

        Claim claim = survival.getClaimHandler().getClaimAt(p.getLocation(), false, omp.getLastClaim());
        if (claim != null)
            omp.setLastClaim(claim);

        if (claim == null || !claim.canAccess(omp, false)) {
            if (p.isFlying() && !timers.containsKey(omp))
                disableFly(omp, true);

        } else if (noFall.containsKey(omp)) {
            p.setAllowFlight(true);
            p.setFlying(true);

            noFall.get(omp).cancel();
            noFall.remove(omp);
        } else if (timers.containsKey(omp)) {
            timers.get(omp).cancel();
            timers.remove(omp);
        }
    }

    private void disableFly(SurvivalPlayer omp, boolean noFall) {
        BossBar bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        bossBar.addPlayer(omp.getPlayer());

        timers.put(omp, new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, 3), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.TICK, 1)) {
            @Override
            public void onInterval() {
                String time = TimeUtils.fromTimeStamp(getRemainingTicks() * 50 + 1000, omp.getLanguage());
                bossBar.setTitle(omp.lang("§f§lFly §c§lgaat uit in " + time + "...", "§c§lDisabling §f§lFly §c§lin " + time + "..."));
                bossBar.setProgress(getProgress());
            }

            @Override
            public void onFinish() {
                bossBar.removeAll();

                omp.getPlayer().setFlying(false);
                omp.getPlayer().setAllowFlight(false);

                new ActionBar(omp, () -> omp.lang("§c§lJe kan alleen vliegen in je claims, of claims waar je §a§l" + Claim.Permission.ACCESS.getName().lang(omp.getLanguage()) + "§c§l hebt.", "§c§lYou are only allowed to fly in your claims, and claims where you have §a§l" + Claim.Permission.ACCESS.getName().lang(omp.getLanguage()) + "§c§l."), 100).send();

                if (!noFall)
                    return;

                FlyEvent.this.noFall.put(omp, new BukkitRunnable() {
                    @Override
                    public void run() {
                        FlyEvent.this.noFall.remove(omp);
                    }
                }.runTaskLater(survival.getOrbitMines(), 150));
            }

            @Override
            public void cancel() {
                super.cancel();
                bossBar.removeAll();
            }
        });
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
