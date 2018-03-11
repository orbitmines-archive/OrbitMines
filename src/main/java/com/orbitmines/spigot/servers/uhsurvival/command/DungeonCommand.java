package com.orbitmines.spigot.servers.uhsurvival.command;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.Dungeon;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.DungeonFile;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.DungeonManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;

/**
 * Created by Robin on 3/11/2018.
 */
public class DungeonCommand extends StaffCommand {

    public DungeonCommand(StaffRank rank) {
        super(rank);
    }

    @Override
    public String[] getAlias() {
        return new String[]{"/d", "/dungeon"};
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "/dungeon [create, delete, info, help, generate]";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        UHPlayer player = UHPlayer.getUHPlayer(omp.getUUID());
        if(player != null){
            DungeonManager dungeonManager = player.getUHWorld().getMap().getDungeons();
            if(dungeonManager != null){
                if(a[1] != null) {
                    MapSection mapSection = player.getSection();

                    String name = a[2];

                    int page = a[2] == null ? 0 : MathUtils.getInteger(a[2]);
                    int minDungeon = (8 * page);
                    int maxDungeon = (8 * (page + 1));

                    switch (a[1]) {
                        case "spawnList":
                            for(int i = minDungeon; i < maxDungeon; i++){
                                Dungeon d = mapSection.getDungeons().get(i);
                                if(d != null) {
                                    player.sendMessage("Dungeon: " + d.getType() + ":" + d.getIndex() + " Loc:" + Serializer.serialize(d.getFirstLocation()));
                                }
                            }
                            break;
                        case "list":
                            for(int i = minDungeon; i < maxDungeon; i++){
                                DungeonFile dungeonFile = (DungeonFile) dungeonManager.getDungeonFiles().toArray()[i];
                                if(dungeonFile != null){
                                    player.sendMessage("Dungeon: " + dungeonFile.getName());
                                }
                            }
                            break;
                        case "reverse":
                            if(!a[3].equalsIgnoreCase("all")) {
                                if (mapSection != null) {
                                    Dungeon dungeon = mapSection.getDungeon(player.getLocation());
                                    if (dungeon != null) {
                                        dungeon.reverse();
                                        dungeonManager.removeDungeon(dungeon);
                                        mapSection.removeDungeon(dungeon);
                                    }
                                }
                            } else {
                                for(Dungeon dungeon : mapSection.getDungeons()){
                                    dungeon.reverse();
                                    dungeonManager.removeDungeon(dungeon);
                                    mapSection.removeDungeon(dungeon);
                                }
                            }
                            break;
                        case "create":
                            if(name != null){
                                //TODO: ADD DUNGEON SELECTOR!
                            }
                            break;
                        case "delete":
                            if(name != null){
                                dungeonManager.deleteDungeon(name);
                            }
                            break;
                        case "info":
                            if(name != null){
                                DungeonFile dungeonFile = dungeonManager.getDungeonFile(name);
                                if(dungeonFile != null){
                                    player.sendMessage("Name: " + dungeonFile.getName());
                                    player.sendMessage("Width:" + dungeonFile.getWidth());
                                    player.sendMessage("Height: " + dungeonFile.getHeight());
                                    player.sendMessage("Depth: " + dungeonFile.getDepth());
                                    player.sendMessage("Y-cords: " + dungeonFile.getMinY() + "-" + dungeonFile.getMaxY());
                                    player.sendMessage("Surface: " + dungeonFile.isSurface());
                                    player.sendMessage("SpawnRate: " + dungeonFile.getSpawnrate());
                                    player.sendMessage("LootTableCommand: " + dungeonFile.getLootTable().getName());
                                }
                            }
                            break;
                        case "generate":
                            if(name != null) {
                                DungeonFile file = dungeonManager.getDungeonFile(name);
                                if (file != null) {
                                    dungeonManager.buildDungeon(player.getLocation(), file);
                                }
                            }
                            break;
                        case "edit":
                            if(name != null) {
                                if (a[3] != null) {
                                    DungeonFile file = dungeonManager.getDungeonFile(name);
                                    if(a[3].equalsIgnoreCase("set")){
                                        if(a[4] != null && a[5] != null){
                                            switch(a[4]){
                                                case "loottable":
                                                    file.setLootTable(a[5]);
                                                    break;
                                                case "minY":
                                                    file.setMinY(MathUtils.getInteger(a[5]) == -1 ? 0 : MathUtils.getInteger(a[5]));
                                                    break;
                                                case "maxY":
                                                    file.setMaxY(MathUtils.getInteger(a[5]) == -1 ? 0 : MathUtils.getInteger(a[5]));
                                                    break;
                                                case "surface":
                                                    file.setSurface(Boolean.parseBoolean(a[5]));
                                                    break;
                                                default:
                                                    player.sendMessage("TODO: NO ARGUMENT FOUND!");
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
                //TODO: SEND MESSAGE
            }
        }
    }
}
