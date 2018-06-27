package com.orbitmines.spigot.api.handlers.npc;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.nms.entity.EntityNms;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public abstract class Npc {

    private static ArrayList<Npc> npcs = new ArrayList<>();

    protected final OrbitMines plugin;
    protected final EntityNms nms;

    protected Location spawnLocation;
    protected InteractAction interactAction;

    protected Set<Player> watchers;

    public Npc(Location spawnLocation) {
        npcs.add(this);
        addToList();

        this.plugin = OrbitMines.getInstance();
        this.nms = plugin.getNms().entity();

        this.spawnLocation = spawnLocation;
    }

    /* SurvivalSpawn the Npc/Entit(y)(ies) in this method */
    protected abstract void spawn();

    /* Despawn the Npc/Entit(y)(ies) in this method */
    protected abstract void despawn();

    /* Update any change to Npc/Entit(y)(ies) in this method */
    public abstract void update();

    /* Return the collection of Npcs/Entit(y)(ies) defined under the 'Npc' */
    protected abstract Collection<? extends Entity> getEntities();

    /* Add to Npc list */
    protected abstract void addToList();

    /* Remove Npc from list */
    protected abstract void removeFromList();

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;

        for (Entity entity : getEntities()) {
            if (entity == null)
                continue;

            /* Only create if the npc has been spawned. */
            create();
            break;
        }
    }

    public boolean isClickable() {
        return interactAction != null;
    }

    public InteractAction getInteractAction() {
        return interactAction;
    }

    /* Add action whenever a player interacts with one of the Entities */
    public void setInteractAction(InteractAction interactAction) {
        this.interactAction = interactAction;
    }

    /* Is null, when visible to all */
    public Set<Player> getWatchers() {
        return watchers;
    }

    public void create() {
        create((Collection<? extends Player>) null);
    }

    public void create(Player createFor) {
        create(Collections.singletonList(createFor));
    }

    public void create(Player... createFor) {
        create(Arrays.asList(createFor));
    }

    /* Create Npc, If you want to create an Npc that is visible to no-one; use an empty collection */
    public void create(Collection<? extends Player> createFor) {
        if (createFor != null) {

            /* From now on the Npc will only be shown to the watchers */
            if (watchers == null)
                watchers = new HashSet<>();

            /* Add all the new watchers to the HashSet */
            watchers.addAll(createFor);
        }

        despawn();
        spawn();

        updateWatchers();
    }

    /* Remove for all players who are not watchers */
    protected void updateWatchers() {
        if (watchers == null)
            return;

        List<Player> hideFor = new ArrayList<>(spawnLocation.getWorld().getPlayers());
        hideFor.removeAll(watchers);

        hideFor(hideFor);
    }

    /* Permanently destroy Npc. */
    public void destroy() {
        despawn();
        removeFromList();
        npcs.remove(this);
    }

    public void hideFor(Player player) {
        hideFor(Collections.singletonList(player));
    }

    public void hideFor(Player... players) {
        hideFor(Arrays.asList(players));
    }

    public void hideFor(Collection<? extends Player> players) {
        /* Add all other players in the world to the watchlist if it is visible to everyone */
        if (watchers == null)
            watchers = new HashSet<>(spawnLocation.getWorld().getPlayers());

        /* Remove players from the watchers */
        watchers.removeAll(players);

        Collection<? extends Entity> entities = getEntities();
        if (entities.size() != 0)
            plugin.getNms().entity().destroyEntitiesFor(entities, players);
    }

    public static List<Npc> getNpcs() {
        return npcs;
    }

    public static Npc getNpc(Entity entity) {
        for (Npc npc : npcs) {
            for (Entity en : npc.getEntities()) {
                if (en == entity) //TODO, might not work?
                    return npc;
            }
        }
        return null;
    }

    public interface InteractAction {

        void onInteract(PlayerInteractEntityEvent event, OMPlayer omp);

    }
}
