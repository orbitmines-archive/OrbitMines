package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmPunishGUI extends GUI {
    
    private final OrbitMines orbitMines;

    private final CachedPlayer player;
    private final Offence offence;
    private final Severity severity;
    private final String reason;

    public ConfirmPunishGUI(CachedPlayer player, Offence offence, Severity severity, String reason) {
        orbitMines = OrbitMines.getInstance();
        this.player = player;
        this.offence = offence;
        this.severity = severity;
        this.reason = reason;

        newInventory(27, "§0§lPunish (" + player.getPlayerName() + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        PunishmentHandler handler = player.getPunishmentHandler();
        handler.update();

        /* Someone else already carried out a punishment while they were */
        if (severity != Severity.WARNING && handler.getActivePunishment(offence.getType()) != null) {
            new PunishGUI(player).open(omp);
            return false;
        }

        ItemInstance cancel = new ItemInstance(new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1, omp.lang("§c§lAnnuleren", "§c§lCancel")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new PunishGUI(player).open(omp);
            }
        };
        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, omp.lang("§a§lBevestigen", "§a§lConfirm")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.getPlayer().closeInventory();
                orbitMines.getServerHandler().getMessageHandler().dataTransfer(
                        PluginMessage.PUNISH,
                        player.getUUID().toString(),
                        offence.toString(),
                        severity.toString(),
                        reason,
                        omp.getUUID().toString());
            }
        };

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (slot % 9 <= 2)
                add(slot, cancel);
            else if (slot % 9 >= 6)
                add(slot, confirm);
        }

        ItemBuilder builder = new PlayerSkullBuilder(() -> player.getPlayerName(), 1,
                player.getRankPrefix() + player.getPlayerName(),
                "",
                "§7" + omp.lang("Overtreding", "Offence") + ": §c§l" + omp.lang(offence.getName()),
                "§7" + omp.lang("Zwaarte", "Severity") + ": §c§l" + omp.lang(severity.getName()),
                "§7" + omp.lang("Reden", "Reason") + ": §c§l" + reason,
                "");

        switch (severity) {

            case SEV_1:
            case SEV_2:
                builder.addLore("§7Type: §c§l" + omp.lang(offence.getType().getName()));
                builder.addLore("§7" + omp.lang("Duur", "Duration") + ": §c§l" + TimeUtils.fromTimeStamp(handler.getMillis(offence, severity), omp.getLanguage()));
                builder.addLore("");
                break;
            case SEV_3:
                builder.addLore("§7Type: §c§l" + omp.lang(offence.getType().getName()));
                builder.addLore("§7" + omp.lang("Duur", "Duration") + ": §c§lPERMANENT");
                builder.addLore("");
                break;
        }

        add(1, 4, new EmptyItemInstance(builder.build()));

        return true;
    }
}
