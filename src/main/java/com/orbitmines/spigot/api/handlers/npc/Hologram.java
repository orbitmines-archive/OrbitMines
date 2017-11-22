package com.orbitmines.spigot.api.handlers.npc;

/*
* OrbitMines, LLC CONFIDENTIAL - @author Fadi Shawki - 2017
* __________________
*
*  2017 OrbitMines, LLC
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of OrbitMines, LLC and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to OrbitMines, LLC
* and its suppliers and may be covered by U.S. and Foreign Patents,
* patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from OrbitMines, LLC.
*/

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.entity.EntityNms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Hologram {

    private static List<Hologram> holograms = new ArrayList<>();

    private OrbitMines orbitMines;
    private EntityNms nms;

    private Location location;
    private List<ArmorStand> armorStands;
    private List<String> lines;
    private Map<String, ArmorStand> displayNames;

    private boolean hideOnJoin;
    private Set<Player> watchers;

    public Hologram(Location location) {
        this(location, false);
    }

    public Hologram(Location location, boolean hideOnJoin) {
        holograms.add(this);

        orbitMines = OrbitMines.getInstance();
        nms = orbitMines.getNms().entity();

        this.location = location.clone().add(0, 1.75, 0);
        this.hideOnJoin = hideOnJoin;
        this.armorStands = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.displayNames = new HashMap<>();
        this.watchers = new HashSet<>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location.clone().add(0, 1.75, 0);
    }

    public boolean hideOnJoin() {
        return hideOnJoin;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public List<String> getLines() {
        return lines;
    }

    public Map<String, ArmorStand> getDisplayNames() {
        return displayNames;
    }

    public void addLine(String line) {
        lines.add(line);
        displayNames.put(line, null);
    }

    public void setLine(int index, String line) {
        if (lines.size() < index)
            return;

        String fromLine = lines.get(index - 1);
        lines.set(index - 1, line);
        displayNames.remove(fromLine);
        displayNames.put(line, null);
    }

    public void updateLine(int index, String line) {
        if (lines.size() < index)
            return;

        String fromLine = lines.get(index - 1);
        lines.set(index - 1, line);

        ArmorStand armorStand = displayNames.get(fromLine);
        armorStand.setCustomName(line);
        armorStand.setCustomNameVisible(true);

        displayNames.remove(fromLine);
        displayNames.put(line, armorStand);
    }

    public void hideLines() {
        int index = 1;
        for (String line : lines) {
            hideLine(index);
            index++;
        }
    }

    public void hideLine(int index) {
        ArmorStand armorStand = displayNames.get(lines.get(index - 1));
        armorStand.setCustomName(null);
        armorStand.setCustomNameVisible(false);
    }

    public void removeLine(int index) {
        if (lines.size() < index)
            return;

        displayNames.remove(lines.get(index - 1));
        lines.remove(index - 1);
    }

    public void clearLines() {
        displayNames.clear();
        lines.clear();
    }

    public void create() {
        create((List<Player>) null);
    }

    public void create(Player... players) {
        create(Arrays.asList(players));
    }

    public void createHideFor(Player... players) {
        List<Player> createFor = new ArrayList<>(Bukkit.getOnlinePlayers());
        createFor.removeAll(Arrays.asList(players));

        create(createFor);
    }

    /* null: all players */
    public void create(Collection<? extends Player> createFor) {
        if (armorStands.size() != 0) {
            delete();
            armorStands.clear();

            createFor = watchers;
        }

        if (location == null)
            return;

        int index = 0;
        for (String displayName : lines) {
            ArmorStand armorStand = orbitMines.getNms().armorStand().spawn(new Location(location.getWorld(), location.getX(), location.getY() - (index * 0.25), location.getZ()), false);
            armorStand.setMarker(true);
            armorStand.setCustomName(displayName);
            armorStand.setCustomNameVisible(true);
            armorStand.setRemoveWhenFarAway(false);
            armorStand.setGravity(false);

            displayNames.put(displayName, armorStand);
            armorStands.add(armorStand);

            index++;

            if (!hideOnJoin || createFor == null)
                continue;

            watchers.addAll(createFor);

            createForWatchers();
        }
    }

    public void createForWatchers() {
        List<Player> hideFor = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (!watchers.contains(player))
                hideFor.add(player);
        }

        hideFor(hideFor);
    }

    public Set<Player> getWatchers() {
        return watchers;
    }

    public void clear() {
        for(ArmorStand as : armorStands){
            as.remove();
        }
    }

    public void delete() {
        clear();
        holograms.remove(this);
    }

    public void hideFor(Player player) {
        hideFor(Collections.singletonList(player));
    }

    public void hideFor(Collection<? extends Player> players) {
        for (ArmorStand armorStand : armorStands) {
            nms.destroyEntityFor(players, armorStand);
        }
    }

    public static Hologram getHologram(ArmorStand armorStand) {
        for (Hologram hologram : holograms) {
            if (hologram.getArmorStands().contains(armorStand))
                return hologram;
        }
        return null;
    }

    public static List<Hologram> getHolograms() {
        return holograms;
    }
}
