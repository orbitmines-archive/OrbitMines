package com.orbitmines.spigot.api.handlers;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.chat.Title;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.Sound;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

/* If a certain class can be teleported to, through channeling */
public abstract class Teleportable {

    public abstract Location getLocation();

    /* In Seconds */
    public abstract int getDuration(OMPlayer omp);

    public abstract Color getColor();

    public abstract String getName();

    public void teleport(OMPlayer omp) {
        if (omp.getTeleportingTo() != null)
            omp.getTeleportingTimer().cancel();

        int duration = getDuration(omp);

        Timer timer = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, duration), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, 1)) {

            @Override
            public void onInterval() {
                int seconds = (int) (getProgress() * ((float) (duration)));

                new Title(new Message(""), new Message("§7§l" + omp.lang("Teleporten naar", "Teleporting to") + " " + getColor().getChatColor() + "§l" + getName() + "§7§l in " + getColor().getChatColor() + "§l" + seconds + "§7§l..."), 0, 40, 0).send(omp);
            }

            @Override
            public void onFinish() {
                omp.setTeleportingTo(null);
                omp.setTeleportingTimer(null);

                Location location = getLocation();
                location.getChunk().load();

                omp.getPlayer().teleport(location);
                omp.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);

                new Title(new Message(""), new Message("§7§l" + omp.lang("Geteleporteerd naar", "Teleported to") + " " + getColor().getChatColor() + "§l" + getName() + "§7§l."), 0, 40, 0).send(omp);
            }
        };
        timer.onInterval();

        omp.sendMessage("Teleporter", Color.LIME, "§7" + omp.lang("Teleporten naar", "Teleporting to") + " " + getColor().getChatColor() + getName() + "§7...");

        omp.setTeleportingTo(this);
        omp.setTeleportingTimer(timer);
    }
}
