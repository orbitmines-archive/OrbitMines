package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SeverityGUI extends GUI {

    private CachedPlayer player;
    private Offence offence;

    public SeverityGUI(CachedPlayer player, Offence offence) {
        this.player = player;
        this.offence = offence;

        newInventory(45, "§0§lPunish (" + player.getPlayerName() + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        PunishmentHandler handler = player.getPunishmentHandler();
        handler.update();

        /* Someone else already carried out a punishment while they were */
        if (handler.getActivePunishment(offence.getType()) != null) {
            new PunishGUI(player).open(omp);
            return false;
        }

        for (Severity severity : Severity.values()) {
            ItemBuilder builder = getIcon(omp, severity);

            Severity realSev;

            if (severity != Severity.WARNING) {
                realSev = handler.getRealSeverity(offence, severity);

                switch (severity) {

                    case SEV_1:
                    case SEV_2:
                        builder.addLore("§7" + omp.lang("Duur", "Duration") + ": §c§l" + TimeUtils.fromTimeStamp(handler.getMillis(offence, realSev), omp.getLanguage()));
                        break;
                    case SEV_3:
                        builder.addLore("§7" + omp.lang("Duur", "Duration") + ": §c§lPERMANENT");
                        break;
                }
            } else {
                realSev = severity;
            }

            add(getSlot(severity), new ItemInstance(builder.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new PunishReasonGUI(player, offence, realSev).open(omp);
                }
            });
        }

        add(4, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Red Arrow Left", 1, omp.lang("§7« Terug", "§7« Back")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new PunishGUI(player).open(omp);
            }
        });

        return true;
    }

    private ItemBuilder getIcon(OMPlayer omp, Severity severity) {
        String name = omp.lang(severity.getName());

        switch (severity) {

            case WARNING:
                return new ItemBuilder(Material.BOOK_AND_QUILL, 1, 0, "§c§l" + name);
            case SEV_1:
                return new ItemBuilder(Material.INK_SACK, 1, 12, "§c§l" + name);
            case SEV_2:
                return new ItemBuilder(Material.INK_SACK, 1, 4, "§c§l" + name);
            case SEV_3:
                return new ItemBuilder(Material.INK_SACK, 1, 1, "§c§l" + name);
            default:
                return new ItemBuilder(Material.STONE, 1, 0, "§c§l" + name);
        }
    }

    private int getSlot(Severity severity) {
        switch (severity) {

            case WARNING:
                return 31;
            case SEV_1:
                return 11;
            case SEV_2:
                return 13;
            case SEV_3:
                return 15;
            default:
                return 0;
        }
    }
}
