package com.orbitmines.spigot.servers.survival.cmds;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.servers.survival.Survival;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class CommandPet extends Command {

    private final Survival survival;

    public CommandPet(Survival survival) {
        super(CommandLibrary.SURVIVAL_PET);

        this.survival = survival;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.getInventory().firstEmpty() == -1) {
            omp.sendMessage("Pet Ticket", Color.RED, "Je inventory zit vol!", "Your inventory is full!");
            return;
        }

        int slot = omp.getInventory().getHeldItemSlot();
        ItemStack inHand = null;
        if (omp.getInventory().getItem(slot) != null)
            inHand = omp.getInventory().getItem(slot);

        omp.getInventory().setItem(slot, survival.getOrbitMines().getNms().customItem().setMetaData(Survival.PET_TICKET.clone().addLore("§7§oNone linked.").build(), "OrbitMines", "createdOn", System.currentTimeMillis() + ""));
        omp.playSound(Sound.ENTITY_ITEM_PICKUP);
        omp.sendMessage("Claim", Color.LIME, "Je hebt de " + Survival.PET_TICKET.getDisplayName() + "§7 ontvangen.", "§7You have received the " + Survival.PET_TICKET.getDisplayName() + "§7.");

        if (inHand != null)
            omp.getInventory().addItem(inHand);
    }
}
