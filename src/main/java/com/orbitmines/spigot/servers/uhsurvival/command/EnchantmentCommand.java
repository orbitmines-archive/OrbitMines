package com.orbitmines.spigot.servers.uhsurvival.command;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;

import java.util.Arrays;

/**
 * Created by Robin on 3/13/2018.
 */
public class EnchantmentCommand extends StaffCommand {

    public EnchantmentCommand(StaffRank rank){
        super(rank);
    }

    @Override
    public String[] getAlias() {
        return new String[]{"/ce"};
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "/ce [list, enchant]";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        UHPlayer player = UHPlayer.getUHPlayer(omp.getUUID());
        if(a[1] != null){
            switch(a[1].toLowerCase()){
                case "list":
                    int page = a[2] != null ? MathUtils.getInteger(a[2]) : 0;
                    for(int i = page * 8; i < (page + 1) * 8; i++){
                        Enchantment ench = Enchantment.values()[i];
                        if(ench != null){
                            omp.sendMessage(" " +i+". " + ench.getName() + " - " + ench.getMaxLevel());
                        }
                    }
                    break;
                case "enchant":
                    Enchantment enchantment = Enchantment.getEnchantment(a[2]);
                    if(enchantment != null){
                        Tool mainHand = player.getUHInventory().getMainHand();
                        if(mainHand != null){
                            if(Arrays.asList(enchantment.getTypes()).contains(mainHand.getType())){
                                int level = MathUtils.getInteger(a[3]);
                                if(0 < level && level <= enchantment.getMaxLevel()){
                                    mainHand.addEnchantment(enchantment, level);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
}
