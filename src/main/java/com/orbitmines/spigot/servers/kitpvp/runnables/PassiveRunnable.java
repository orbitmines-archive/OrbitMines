package com.orbitmines.spigot.servers.kitpvp.runnables;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.nms.Nms;
import com.orbitmines.spigot.api.nms.entity.EntityNms;
import com.orbitmines.spigot.api.runnables.PlayerRunnable;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.DummyEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PassiveRunnable extends PlayerRunnable {

    private final KitPvP kitPvP;
    private final Nms nms;

    public PassiveRunnable(KitPvP kitPvP) {
        super(SpigotRunnable.TimeUnit.SECOND, 1);

        this.kitPvP = kitPvP;
        this.nms = kitPvP.getOrbitMines().getNms();
    }

    @Override
    public void run(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (omp.getSelectedKit() == null || omp.isSpectator())
            return;

        for (ItemStack item : omp.getCompleteInventory()) {
            if (item == null)
                continue;

            Map<Passive, Integer> passives = Passive.from(nms.customItem(), item);

            /* No Passives on this item */
            if (passives == null)
                continue;

            for (Passive passive : passives.keySet()) {
                if (passive == Passive.ARROW_REGEN) {
                    passive.getHandler().trigger(new DummyEvent(kitPvP, omp), passives.get(passive));
                } else if (passive.getHandler() instanceof Passive.LowHealthHandler) {
                    DummyEvent event = new DummyEvent(kitPvP, omp);
                    int level = passives.get(passive);

                    Passive.LowHealthHandler lowHealthHandler = (Passive.LowHealthHandler) passive.getHandler();
                    Player p = omp.getPlayer();

                    if (p.getHealth() / nms.entity().getAttribute(p, EntityNms.Attribute.MAX_HEALTH) > lowHealthHandler.getPercentage(level))
                        lowHealthHandler.triggerOff(event, level);
                    else
                        lowHealthHandler.trigger(event, level);
                }
            }
        }
    }
}
