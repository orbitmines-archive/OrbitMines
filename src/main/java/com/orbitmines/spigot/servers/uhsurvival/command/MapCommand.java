package com.orbitmines.spigot.servers.uhsurvival.command;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.warzone.Warzone;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ChestRarity;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by Robin on 3/13/2018.
 */
public class MapCommand extends StaffCommand {

    public MapCommand(StaffRank rank) {
        super(rank);
    }

    @Override
    public String[] getAlias() {
        return new String[]{"/map"};
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "/map";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        UHPlayer player = UHPlayer.getUHPlayer(omp.getUUID());
        if(a[1] != null){
            switch(a[1].toLowerCase()){
                case "resize":
                    if(a[2] != null){
                        switch(a[2].toLowerCase()){
                            case "section":
                                if(a[3] != null && a[4] != null){
                                    int width = MathUtils.getInteger(a[3]);
                                    int heigth = MathUtils.getInteger(a[4]);
                                    if(width <= 0 && heigth <= 0){
                                        player.getUHWorld().getMap().setWidthHeightSection(width, heigth);
                                    }
                                }
                                break;
                            case "map":
                                if(a[3] != null && a[4] != null){
                                    int width = MathUtils.getInteger(a[3]);
                                    int heigth = MathUtils.getInteger(a[4]);
                                    if(width <= 0 && heigth <= 0){
                                        player.getUHWorld().getMap().setWidthHeight(width, heigth);
                                    }
                                }
                                break;
                        }
                    }
                    break;
                case "spawn":
                    if(player.getUHWorld().getMap().hasWarzone()) {
                        if (a[2] != null) {
                            ChestRarity rarity = ChestRarity.getRarity(a[2].toLowerCase());
                            if (rarity != null) {
                                player.getUHWorld().getMap().getWarzone().addChest(rarity);
                            }
                        } else {
                            player.getUHWorld().getMap().getWarzone().addChest();
                        }
                    }
                    break;
                case "edit": // /map edit <player>
                    if(player.getUHWorld().getMap().hasWarzone()) {
                        UUID id;
                        if (a[2] != null) {
                            id = Bukkit.getPlayer(a[2]).getUniqueId();
                        } else {
                            id = player.getUUID();
                        }
                        Warzone warzone = player.getUHWorld().getMap().getWarzone();
                        if(warzone.canEdit(id)){
                            warzone.removePlayer(id);
                        } else {
                            warzone.addEditor(id);
                        }
                    }
                    break;
            }
        }
    }
}
