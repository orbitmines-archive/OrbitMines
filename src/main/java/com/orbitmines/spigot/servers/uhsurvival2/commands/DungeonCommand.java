package com.orbitmines.spigot.servers.uhsurvival2.commands;

import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.DungeonFile;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.map.dungeon.DungeonManager;

public class DungeonCommand extends StaffCommand {

    private String[] alias = {"/dungeon", "/d"};

    private static final String[] presets = {"minY", "maxY", "loottable", "surface", "spawnrate", "name"};

    public DungeonCommand() {
        super(Server.UHSURVIVAL, StaffRank.MODERATOR);
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        UHPlayer player = (UHPlayer) omp;
        if (a.length != 1) {
            switch (a[1].toLowerCase()) {
                case "c": {
                    if (a.length == 4) {
                        if (DungeonManager.getDungeonSelector().hasSelectedArea(player)) {
                            DungeonManager.createDungeon(player, a[2], a[3]);
                            player.sendMessage("dungeon created!");
                        } else {
                            player.sendMessage("no area selected!");
                        }
                    } else {
                        player.sendMessage("not enough arguments!");
                    }
                }
                break;
                case "d": {
                    if (a.length == 3) {
                        if (player.getMapLocation().getMap().getDM().deleteDungeon(a[2])) {
                            player.sendMessage("dungeon did delete!");
                        } else {
                            player.sendMessage("dungeon did not delete!");
                        }
                    } else {
                        player.sendMessage("not enough arguments!");
                    }
                }
                break;
                case "e": {
                    if (a[2].equalsIgnoreCase("list")) {
                        for (String preset : presets) {
                            player.sendMessage(preset);
                        }
                    } else {
                        DungeonFile file = player.getMapLocation().getMap().getDM().getDungeonFile(a[2]);
                        if (file != null) {
                            String value = a[4];
                            if (value != null) {
                                switch (a[3].toLowerCase()) {
                                    case "miny":
                                        file.setMinY(MathUtils.getInteger(value));
                                        break;
                                    case "maxy":
                                        file.setMaxY(MathUtils.getInteger(value));
                                        break;
                                    case "loottable":
                                        file.setLootTable(value);
                                        break;
                                    case "surface":
                                        file.setSurface(Boolean.parseBoolean(value));
                                        break;
                                    case "spawnrate":
                                        file.setSpawnrate(MathUtils.getDouble(value));
                                        break;
                                    case "name":
                                        player.sendMessage("needs to be added!");
                                        break;
                                    default:
                                        break;

                                }
                                player.sendMessage(String.format("%s has been updated!", a[2]));
                            } else {
                                player.sendMessage("value cannot be null!");
                            }
                        } else {
                            player.sendMessage("no dungeon Found!");
                        }
                    }
                }
                break;
                case "s":
                    if (a.length == 3) {
                        if (player.getMapLocation().getMap().getDM().buildDungeon(player, a[2])) {
                            player.sendMessage("dungeon has been build!");
                        } else {
                            player.sendMessage("dungeon failed to be build!");
                        }
                    }
                    break;
            }
        }
    }

    /*
    *   -/d <c/d/e>
    *
    *   -/d c <name> <loottable>
    *
    *   -/d d <name>
    *
    *   -/d e <name> <preset> <value>
    *
    *   -/d e list
    *
    *   -/d s <name>
    *
    * */

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }
}
