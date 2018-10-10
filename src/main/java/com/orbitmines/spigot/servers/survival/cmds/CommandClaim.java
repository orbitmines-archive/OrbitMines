package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class CommandClaim extends Command {

    public CommandClaim() {
        super(CommandLibrary.SURVIVAL_CLAIM);
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.getInventory().firstEmpty() == -1) {
            omp.sendMessage("Claim", Color.RED, "Je inventory zit vol!", "Your inventory is full!");
            return;
        }

        int slot = omp.getInventory().getHeldItemSlot();
        ItemStack inHand = null;
        if (omp.getInventory().getItem(slot) != null)
            inHand = omp.getInventory().getItem(slot);

        omp.getInventory().setItem(slot, Claim.CLAIMING_TOOL.build());
        omp.playSound(Sound.ENTITY_ITEM_PICKUP);
        omp.sendMessage("Claim", Color.LIME, "Je hebt de " + Claim.CLAIMING_TOOL.getDisplayName() + "§7 ontvangen.", "§7You have received the " + Claim.CLAIMING_TOOL.getDisplayName() + "§7.");

        if (inHand != null)
            omp.getInventory().addItem(inHand);
    }
}
