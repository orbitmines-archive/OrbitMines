package com.orbitmines.spigot.api.handlers.npc;

import com.madblock.api.utils.RandomUtils;
import com.madblock.spigot.MadBlock;
import com.madblock.spigot.api.Direction;
import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.nms.bednpc.BedNpcNms;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class BedNpc {

    private BedNpcNms nms;

    private final Player player;
    private final Direction direction;
    private final Location location;
    private final boolean firstPerson;
    private final boolean withItemsInHand;
    private final boolean withArmor;
    private final Set<Player> watchers;
    private int entityId;

    public BedNpc(Player player, Location location, boolean firstPerson) {
        this(player, location, firstPerson, false, false);
    }

    public BedNpc(Player player, Location location, boolean firstPerson, boolean withItemsInHand) {
        this(player, location, firstPerson, withItemsInHand, false);
    }

    public BedNpc(Player player, Location location, boolean firstPerson, boolean withItemsInHand, boolean withArmor) {
        nms = MadBlock.getInstance().getNms().bedNpc();

        this.player = player;
        this.location = location;
        this.firstPerson = firstPerson;
        this.withItemsInHand = withItemsInHand;
        this.withArmor = withArmor;
        this.watchers = new HashSet<>();
        this.entityId = -1;

        Location fixed = nms.getFixedLocation(this);
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Block b = fixed.getWorld().getBlockAt(direction.getAsNewLocation(fixed));

            if (b == null || b.getType() == Material.AIR)
                directions.add(direction);
        }

        if (directions.size() == 0)
            directions = Arrays.asList(Direction.values());

        this.direction = RandomUtils.randomFrom(directions);
    }

    public Player getPlayer() {
        return player;
    }

    public Direction getDirection() {
        return direction;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isFirstPerson() {
        return firstPerson;
    }

    public boolean withItemsInHand() {
        return withItemsInHand;
    }

    public boolean withArmor() {
        return withArmor;
    }

    public Set<Player> getWatchers() {
        return watchers;
    }

    public int getEntityId() {
        return entityId;
    }

    public void show(Player... players) {
        show(Arrays.asList(players));
    }

    public void show(Collection<? extends Player> watchers) {
        this.watchers.addAll(watchers);

        entityId = nms.spawn(this, withArmor, withItemsInHand);
    }

    public void destroy() {
        OMPlayer.getPlayer(player).clearFreeze();
        nms.destroy(this);
    }
}
