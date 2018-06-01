package com.orbitmines.spigot.servers.survival.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

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

        int x1 = claim.getCorner1().getBlockX();
        int x2 = claim.getCorner2().getBlockX();
        int z1 = claim.getCorner1().getBlockZ();
        int z2 = claim.getCorner2().getBlockZ();

        int width = (Math.abs(x1 - x2) + 1);
        int height = (Math.abs(z1 - z2) + 1);
        int area = width * height;

        int x = x1 > x2 ? x1 - width : x1 + width;
        int z = z1 > z2 ? z1 - height : z1 + height;

        add(2, 4, new EmptyItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.lang("§c§lVerwijder Claim", "§c§lRemove Claim")).build()));

        add(3, 4, new EmptyItemInstance(new ItemBuilder(Material.STONE_HOE, 1, 0, "§a§lClaim #" + NumberUtils.locale(claim.getId()),
                "§7Created on: §a§l" + DateUtils.SIMPLE_FORMAT.format(claim.getCreatedOn()),
                "",
                "§7" + omp.lang("Oppervlak", "Area") + ": §a§l" + NumberUtils.locale(width) + " x " + NumberUtils.locale(height),
                "§7Blocks: §a§l" + NumberUtils.locale(area),
                "§7XZ: §a§l" + x + " §7/ §a§l" + z
        ).addFlag(ItemFlag.HIDE_ATTRIBUTES).build()));

        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5, omp.lang("§a§lBevestigen", "§a§lConfirm")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                survival.getClaimHandler().abandonClaim(claim, (SurvivalPlayer) omp, true);
                omp.getPlayer().closeInventory();
            }
        };
        ItemInstance cancel = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14, omp.lang("§c§lAnnuleer", "§c§lCancel")).build()) {
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
