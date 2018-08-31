package com.orbitmines.spigot.servers.survival.gui.claim;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClaimRemovalGUI extends GUI {

    private final Survival survival;
    private final Claim claim;

    public ClaimRemovalGUI(Survival survival, Claim claim) {
        this.survival = survival;
        this.claim = claim;

        newInventory(54, "§0§lAre you sure?");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        add(2, 4, new EmptyItemInstance(new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lVerwijder Claim", "§c§lRemove Claim")).build()));

        add(3, 4, new EmptyItemInstance(ClaimGUI.getClaimIcon(omp, claim).build()));

        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, omp.lang("§a§lBevestigen", "§a§lConfirm")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                survival.getClaimHandler().abandonClaim(claim, (SurvivalPlayer) omp, true);
                omp.getPlayer().closeInventory();
            }
        };
        ItemInstance cancel = new ItemInstance(new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1, omp.lang("§c§lAnnuleer", "§c§lCancel")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new ClaimGUI(survival, claim).open(omp);
            }
        };

        for (int i = 0; i < 9; i++) {
            if (i > 2 && i < 6)
                continue;

            for (int j = 1; j < 5; j++) {
                add(j, i, i <= 2 ? confirm : cancel);
            }
        }

        return true;
    }
}
