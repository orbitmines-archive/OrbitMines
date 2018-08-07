package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ReasonGUI {

    private final OrbitMines orbitMines;

    protected final CachedPlayer player;
    protected final Offence offence;
    protected final Severity severity;

    public ReasonGUI(CachedPlayer player) {
        this(player, null, null);
    }

    public ReasonGUI(CachedPlayer player, Offence offence, Severity severity) {
        this.orbitMines = OrbitMines.getInstance();
        this.player = player;
        this.offence = offence;
        this.severity = severity;
    }

    public abstract void openReasonGUI(OMPlayer omp, String reason);

    public void open(OMPlayer omp) {
        omp.setLastInventory(null);

        AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
            String reason = event.getName();

            if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT || reason.equals("")) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                return;
            }

            event.setWillClose(true);
            event.setWillDestroy(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    openReasonGUI(omp, reason);
                }
            }.runTaskLater(orbitMines, 2);

        }, new AnvilNms.AnvilCloseEvent() {
            @Override
            public void onClose() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new PunishGUI(player).open(omp);
                    }
                }.runTaskLater(orbitMines, 1);
            }
        });

        anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.SIGN, 1, omp.lang("Reden", "Reason")).build());

        anvil.open();
    }
}
