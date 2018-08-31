package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;
import com.orbitmines.spigot.api.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CommandHat extends VipCommand {

    private String[] alias = { "/hat" };

    public CommandHat() {
        super(Server.SURVIVAL, VipRank.DIAMOND);
    }//TODO EMERALD+?

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        PlayerInventory inventory = omp.getPlayer().getInventory();

        ItemStack inHand = inventory.getItemInMainHand();

        if (inHand == null) {
            omp.sendMessage("Hat", "Je hebt geen item in je hand!", "You don't have an item in your hand!");
            return;
        }

        ItemStack helmet = inventory.getHelmet();

        inventory.setHelmet(inHand);
        inventory.setItemInMainHand(helmet);

        String name = ItemUtils.getName(inHand.getType());

        omp.sendMessage("Hat", "Je hebt een §a§l" + name + "§7 op je hoofd gezet.", "You have set a(n) §a§l" + name + "§7 on your head.");
    }
}
