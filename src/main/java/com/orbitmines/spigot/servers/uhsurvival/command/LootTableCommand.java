package com.orbitmines.spigot.servers.uhsurvival.command;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.DungeonManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTable;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTableManager;

/**
 * Created by Robin on 3/11/2018.
 */
public class LootTableCommand extends StaffCommand {

    private LootTableManager lootTableManager;

    public LootTableCommand(StaffRank rank) {
        super(rank);
        lootTableManager = DungeonManager.getLootTableManager();
    }

    @Override
    public String[] getAlias() {
        return new String[]{"/loot", "/loottable"};
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        UHPlayer player = UHPlayer.getUHPlayer(omp.getUUID());
        if (a[1] != null) {
            //TODO: POPULATE
            String name = a[2];
            switch (a[1]) {
                case "create":
                    if(name != null){
                        lootTableManager.createLootTable(omp.getWorld(), name);
                    }
                    break;
                case "delete":
                    if(name != null) {
                        lootTableManager.removeLootTable(name);
                    }
                    break;
                case "set":
                    break;
                case "setItem":
                    break;
                case "add":
                    break;
                case "list":
                    if(name != null) {
                        int page = MathUtils.getInteger(a[2]) == -1 ? 0 : MathUtils.getInteger(a[2]);
                        for(int i = page * 8; i < 8 * (page + 1); i++){
                            LootTable lootTable = (LootTable) lootTableManager.getLootTables().toArray()[i];
                            if(lootTable != null) {
                                player.sendMessage(i+"." + " Name: " + lootTable.getName() + " Content: " + lootTable.getLootItems().size());
                            }
                        }
                    }
                    break;
                case "content":
                    break;
                default:
                    //TODO: NO ARGUMENT!
                    break;
            }
        }
    }
}
