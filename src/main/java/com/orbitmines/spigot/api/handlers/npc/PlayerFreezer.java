package com.orbitmines.spigot.api.handlers.npc;

import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class PlayerFreezer extends ArmorStandNpc {

    private static ArrayList<PlayerFreezer> freezers = new ArrayList<>();

    private final Player player;
    private final SpigotRunnable runnable;

    public PlayerFreezer(Player player) {
        this(player, player.getLocation().subtract(0, 1, 0)); //TODO right subtraction?
    }

    public PlayerFreezer(Player player, Location spawnLocation) {
        super(spawnLocation);

        this.player = player;
        this.gravity = false;

        spawn();

        runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run() {
                if (!armorStand.getPassengers().contains(player))
                    armorStand.addPassenger(player);
            }
        };
    }

    @Override
    protected void spawn() {
        super.spawn();

        nms.setInvisible(armorStand, true);
    }

    @Override
    protected void addToList() {
        super.addToList();

        /* Clear previous */
        PlayerFreezer prev = getFreezer(player);
        if (prev != null)
            prev.destroy();

        freezers.add(this);
    }

    @Override
    protected void removeFromList() {
        super.removeFromList();
        freezers.remove(this);
    }

    @Override
    public void destroy() {
        super.destroy();

        runnable.cancel();
    }

    public Player getPlayer() {
        return player;
    }

    public SpigotRunnable getRunnable() {
        return runnable;
    }

    public static List<PlayerFreezer> getFreezers() {
        return freezers;
    }

    public static PlayerFreezer getFreezer(Player player) {
        for (PlayerFreezer freezer : freezers) {
            if (freezer.player == player)
                return freezer;
        }
        return null;
    }
}
