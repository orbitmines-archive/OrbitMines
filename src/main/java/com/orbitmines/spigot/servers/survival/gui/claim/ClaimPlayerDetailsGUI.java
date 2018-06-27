package com.orbitmines.spigot.servers.survival.gui.claim;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClaimPlayerDetailsGUI extends GUI {

    private final Survival survival;
    private final Claim claim;
    private final CachedPlayer member;

    public ClaimPlayerDetailsGUI(Survival survival, Claim claim, CachedPlayer member) {
        this.survival = survival;
        this.claim = claim;
        this.member = member;

        newInventory(27, "§0§lClaim - " + member.getPlayerName());
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        if (player.getUUID().equals(member.getUUID())) {
            player.sendMessage("Claim", Color.RED, "§7Je kan jezelf niet aan je eigen claim toevoegen. :(", "§7You cannot add yourself to your own claim. :(");
            return false;
        }

        SurvivalPlayer omp = (SurvivalPlayer) player;

        Claim.Permission current = claim.getMembers().getOrDefault(member.getUUID(), null);

        int index = 0;
        for (Claim.Permission permission : Claim.Permission.values()) {
            String color = current == permission ? "§a" : "§7";
            ItemBuilder item = permission.getIcon().setDisplayName(color + "§l" + permission.getName().lang(omp.getLanguage()));

            if (current == permission)
                item.glow();

            for (Message desc : permission.getDescription()) {
                item.addLore("§7- " + color + desc.lang(omp.getLanguage()));
            }

            add(1, (current != null ? 1 : 2) + index, new ItemInstance(item.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    claim.getMembers().put(member.getUUID(), permission);
                    survival.getClaimHandler().saveClaim(claim);

                    if (current != null)
                        reopen(omp);
                    else
                        /* Added for the first time, so back to claim */
                        new ClaimGUI(survival, claim).open(omp);
                }
            });

            index++;
        }

        add(1, current != null ? 5 : 6, new ItemInstance(new PlayerSkullBuilder(member::getPlayerName, 1, omp.lang("§a§lTerug naar Claim #", "§a§lBack to Claim #") + NumberUtils.locale(claim.getId())).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new ClaimGUI(survival, claim).open(omp);
            }
        });

        if (current != null)
            add(1, 7, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.lang("§c§lVerwijder Speler van Claim", "§c§lRemove Player from Claim")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    claim.getMembers().remove(member.getUUID());
                    survival.getClaimHandler().saveClaim(claim);

                    new ClaimGUI(survival, claim).open(omp);
                }
            });

        return true;
    }
}
