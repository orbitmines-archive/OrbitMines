package com.orbitmines.spigot.api.handlers.npc;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class PlayerFreezer extends NpcArmorStand {

    private static List<PlayerFreezer> freezers = new ArrayList<>();

    private final Player player;
    private final SpigotRunnable runnable;

    public PlayerFreezer(Player player) {
        this(player, player.getLocation().subtract(0, 1, 0));
    }

    public PlayerFreezer(Player player, Location location) {
        super(location, false);

        /* Clear previous */
        PlayerFreezer prev = getFreezer(player);
        if (prev != null)
            prev.delete();

        freezers.add(this);

        this.player = player;

        setGravity(false);

        spawn();

        runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run() {
                if (!getArmorStand().getPassengers().contains(player))
                    getArmorStand().addPassenger(player);
            }
        };
    }

    @Override
    public void spawn() {
        super.spawn();

        OrbitMines.getInstance().getNms().entity().setInvisible(getArmorStand(), true);
    }

    @Override
    public void delete() {
        super.delete();

        runnable.cancel();
        freezers.remove(this);
    }

    public Player getPlayer() {
        return player;
    }

    public SpigotRunnable getRunnable() {
        return runnable;
    }

    public static PlayerFreezer getFreezer(Player player) {
        for (PlayerFreezer freezer : freezers) {
            if (freezer.player == player)
                return freezer;
        }
        return null;
    }

    public static List<PlayerFreezer> getFreezers() {
        return freezers;
    }
}
