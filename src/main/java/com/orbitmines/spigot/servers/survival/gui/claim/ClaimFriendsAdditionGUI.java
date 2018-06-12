package com.orbitmines.spigot.servers.survival.gui.claim;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.ColorUtils;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

public class ClaimFriendsAdditionGUI extends GUI {

    private final Survival survival;
    private final Claim claim;
    private final Claim.Permission permission;
    private final ClaimGUI.FriendType type;
    private final List<UUID> toAdd;

    public ClaimFriendsAdditionGUI(Survival survival, Claim claim, Claim.Permission permission, ClaimGUI.FriendType type, List<UUID> toAdd) {
        this.survival = survival;
        this.claim = claim;
        this.permission = permission;
        this.type = type;
        this.toAdd = toAdd;

        newInventory(54, "§0§lAre you sure?");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        add(2, 4, new EmptyItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, ColorUtils.getWoolData(type.getColor()), type.getColor().getChatColor() + "§l" + type.getTitle().lang(omp.getLanguage())).build()));

        {
            ItemBuilder item = permission.getIcon().setDisplayName("§7§l" + permission.getName().lang(omp.getLanguage()));

            for (Message desc : permission.getDescription()) {
                item.addLore("§7- " + desc.lang(omp.getLanguage()));
            }

            add(3, 4, new EmptyItemInstance(item.build()));
        }

        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5, omp.lang("§a§lBevestigen", "§a§lConfirm")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                for (UUID uuid : toAdd) {
                    claim.getMembers().put(uuid, permission);
                }

                survival.getClaimHandler().saveClaim(claim);

                new ClaimGUI(survival, claim).open(omp);
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
