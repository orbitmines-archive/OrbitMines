package com.orbitmines.spigot.api.handlers;

import com.orbitmines.spigot.api.Direction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Letters {

    private String word;
    private Direction direction;
    private Location location;

    public Letters(String word, Direction direction, Location location) {
        this.word = word;
        this.direction = direction;
        this.location = location;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void clear() {
        generate(Material.AIR);
    }

    public void generate(Material material) {
        generate(material);

        List<Location> locations = getLocations(getStartingLocation(location));

        for (Location loc : locations) {
            Block b = location.getWorld().getBlockAt(loc);
            b.setType(material);
        }
    }

    public void clear(Player... players) {
        generate(Material.AIR, players);
    }

    public void generate(Material material, Player... players) {
        generate(material, Arrays.asList(players));
    }

    public void clear(List<Player> players) {
        generate(Material.AIR, players);
    }

    public void generate(Material material, List<Player> players) {
        List<Location> locations = getLocations(getStartingLocation(location));

        for (Player p : players) {
            for (Location loc : locations) {
                p.getPlayer().sendBlockChange(loc, material.createBlockData());
            }
        }
    }

    private List<Location> getLocations(Location location) {
        List<Location> locations = new ArrayList<>();

        int index = 0;
        String lastLetter = null;
        for (String letter : word.toUpperCase().split("")) {
            Location l = location.clone();

            if (index != 0) {
                int nextLetter = 4;
                if (lastLetter != null && (lastLetter.equals(":") || lastLetter.equals(".") || lastLetter.equals(" ") || lastLetter.equals("'"))) {
                    nextLetter = 2;
                }

                switch (direction) {
                    case NORTH:
                        l.add(nextLetter, 0, 0);
                        break;
                    case EAST:
                        l.add(0, 0, nextLetter);
                        break;
                    case SOUTH:
                        l.subtract(nextLetter, 0, 0);
                        break;
                    case WEST:
                        l.subtract(0, 0, nextLetter);
                        break;
                }
            }

            locations.addAll(getLocations(l, letter));

            lastLetter = letter;
            index++;
        }

        return locations;
    }

    private List<Location> getLocations(Location location, String letter) {
        World w = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        if (letter.equals("A")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("B")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 1, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 1, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 1));
            }
        } else if (letter.equals("C")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1));
            }
        } else if (letter.equals("D")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 1, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 1, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 1));
            }
        } else if (letter.equals("E")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals("F")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals("G")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2));
            }
        } else if (letter.equals("H")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("I")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 1, z), new Location(w, x + 1, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z + 1), new Location(w, x, y + 3, z + 1), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 1, z), new Location(w, x - 1, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z - 1), new Location(w, x, y + 3, z - 1), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals("J")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2));
            }
        } else if (letter.equals("K")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("L")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1));
            }
        } else if (letter.equals("M")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 3, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 3, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 3, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 3, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("N")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("O")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("P")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("Q")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x + 2, y, z), new Location(w, x + 2, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x - 2, y, z), new Location(w, x - 2, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 1, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("R")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("S")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2));
            }
        } else if (letter.equals("T")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x + 1, y + 1, z), new Location(w, x + 1, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y + 1, z + 1), new Location(w, x, y + 3, z + 1), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x - 1, y + 1, z), new Location(w, x - 1, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y + 1, z - 1), new Location(w, x, y + 3, z - 1), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals("U")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("V")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("W")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 1, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 1, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 1, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 1, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("X")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y, z - 2));
            }
        } else if (letter.equals("Y")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x + 1, y, z), new Location(w, x + 1, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z + 1), new Location(w, x, y + 1, z + 1), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x - 1, y, z), new Location(w, x - 1, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x - 1, y, z), new Location(w, x - 1, y + 1, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 3, z));
            }
        } else if (letter.equals("Z")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x + 2, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x - 2, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals(":")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 3, z));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 3, z));
            }
        } else if (letter.equals("0")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("1")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 1, z), new Location(w, x + 1, y + 3, z), new Location(w, x, y + 3, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z + 1), new Location(w, x, y + 3, z + 1), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 1, z), new Location(w, x - 1, y + 3, z), new Location(w, x, y + 3, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z - 1), new Location(w, x, y + 3, z - 1), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals("2")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x, y + 1, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x, y + 1, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z));
            }
        } else if (letter.equals("3")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2));
            }
        } else if (letter.equals("4")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x + 2, y, z), new Location(w, x + 2, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 2, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x - 2, y, z), new Location(w, x - 2, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 2, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals("5")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2));
            }
        } else if (letter.equals("6")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x, y + 1, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 1, z));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x, y + 1, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 1, z));
            }
        } else if (letter.equals("7")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x + 2, y, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x, y + 4, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 4, z + 2), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z));
                case SOUTH:
                    return Arrays.asList(new Location(w, x - 2, y, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x, y + 4, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 4, z - 2), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z));
            }
        } else if (letter.equals("8")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z), new Location(w, x + 1, y + 2, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2), new Location(w, x, y + 2, z + 1));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z), new Location(w, x - 1, y + 2, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 1, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2), new Location(w, x, y + 2, z - 1));
            }
        } else if (letter.equals("9")) {
            switch (direction) {
                case NORTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x + 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x + 1, y + 4, z), new Location(w, x + 2, y + 4, z), new Location(w, x + 2, y, z), new Location(w, x + 1, y, z), new Location(w, x + 2, y + 2, z), new Location(w, x + 2, y + 1, z), new Location(w, x + 2, y + 3, z));
                case EAST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z + 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z + 1), new Location(w, x, y + 4, z + 2), new Location(w, x, y, z + 2), new Location(w, x, y, z + 1), new Location(w, x, y + 2, z + 2), new Location(w, x, y + 1, z + 2), new Location(w, x, y + 3, z + 2));
                case SOUTH:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x - 1, y + 2, z), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x - 1, y + 4, z), new Location(w, x - 2, y + 4, z), new Location(w, x - 2, y, z), new Location(w, x - 1, y, z), new Location(w, x - 2, y + 2, z), new Location(w, x - 2, y + 1, z), new Location(w, x - 2, y + 3, z));
                case WEST:
                    return Arrays.asList(new Location(w, x, y, z), new Location(w, x, y + 2, z - 1), new Location(w, x, y + 2, z), new Location(w, x, y + 3, z), new Location(w, x, y + 4, z), new Location(w, x, y + 4, z - 1), new Location(w, x, y + 4, z - 2), new Location(w, x, y, z - 2), new Location(w, x, y, z - 1), new Location(w, x, y + 2, z - 2), new Location(w, x, y + 1, z - 2), new Location(w, x, y + 3, z - 2));
            }
        } else if (letter.equals(".")) {
            return Collections.singletonList(new Location(w, x, y, z));
        } else if (letter.equals(":")) {
            return Arrays.asList(new Location(w, x, y + 1, z), new Location(w, x, y + 3, z));
        } else if (letter.equals("'")) {
            return Arrays.asList(new Location(w, x, y + 3, z), new Location(w, x, y + 4, z));
        }

        return new ArrayList<>();
    }

    private Location getStartingLocation(Location centerLocation) {
        double wordLength = 0;
        for (String letter : word.toUpperCase().split("")) {
            int nextLetter = 4;
            if (letter != null && (letter.equals(":") || letter.equals(".") || letter.equals(" ") || letter.equals("'"))) {
                nextLetter = 2;
            }
            wordLength += nextLetter;
        }
        Location startingLocation = centerLocation.clone();
        long offset = Math.round(wordLength / 2);
        switch (direction) {
            case NORTH:
                startingLocation.add(-offset, 0, 0);
                break;
            case EAST:
                startingLocation.add(0, 0, -offset);
                break;
            case SOUTH:
                startingLocation.subtract(-offset, 0, 0);
                break;
            case WEST:
                startingLocation.subtract(0, 0, -offset);
                break;
        }
        return startingLocation;
    }
}
