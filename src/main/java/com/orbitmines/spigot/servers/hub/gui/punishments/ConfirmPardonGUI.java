package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.punishment.Punishment;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmPardonGUI extends GUI {

    private final OrbitMines orbitMines;

    private final CachedPlayer player;
    private final Punishment active;
    private final String reason;

    public ConfirmPardonGUI(CachedPlayer player, Punishment active, String reason) {
        orbitMines = OrbitMines.getInstance();
        this.player = player;
        this.active = active;
        this.reason = reason;

        newInventory(27, "§0§lPardon (" + player.getPlayerName() + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        PunishmentHandler handler = player.getPunishmentHandler();
        handler.update();

        /* Someone else already carried out a punishment while they were */
        if (handler.getActivePunishment(active.getOffence().getType()) == null) {
            new PunishGUI(player).open(omp);
            return false;
        }

        ItemInstance cancel = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14, omp.lang("§c§lAnnuleren", "§c§lCancel")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new PunishGUI(player).open(omp);
            }
        };
        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5, omp.lang("§a§lBevestigen", "§a§lConfirm")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.getPlayer().closeInventory();
                orbitMines.getServerHandler().getMessageHandler().dataTransfer(
                        PluginMessage.PARDON,
                        player.getUUID().toString(),
                        active.getOffence().toString(),
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

        CachedPlayer punishedBy = active.getPunishedBy();

        add(1, 4, new EmptyItemInstance(new PlayerSkullBuilder(() -> player.getPlayerName(), 1,
                player.getRankPrefix() + player.getPlayerName(),
                "§7" + omp.lang("Overtreding", "Offence") + ": §c§l" + omp.lang(active.getOffence().getName()),
                "§7" + omp.lang("Zwaarte", "Severity") + ": §c§l" + omp.lang(active.getSeverity().getName()),
                "§7" + omp.lang("Van", "From") + ": §c§l" + active.getFromString(DateUtils.FORMAT),
                "§7" + omp.lang("Tot", "To") + ": §c§l" + (active.getSeverity().getDuration() == Punishment.Duration.PERMANENT ? "§lPERMANENT" : active.getToString(DateUtils.FORMAT) + "\n§7" + omp.lang("Verloopt over", "Expires in") + ": §c" + active.getExpireInString(omp.getLanguage())),
                "§7" + omp.lang("Gestraft door", "Punished By") + ": " + punishedBy.getRankPrefix() + punishedBy.getPlayerName(),
                "§7" + omp.lang("Reden", "Reason") + ": §c§l" + active.getReason()).build()));

        return true;
    }
}
