package com.orbitmines.spigot.servers.kitpvp.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

public class ProjectileEvents implements Listener {

    private final KitPvP kitPvP;
    private final ItemStackNms nms;

    public ProjectileEvents(KitPvP kitPvP) {
        this.kitPvP = kitPvP;
        this.nms = kitPvP.getOrbitMines().getNms().customItem();
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        ItemStack bow = event.getBow();

        Map<Passive, Integer> passives = Passive.from(nms, bow, Passive.Interaction.APPLY_TO_ARROW);

        /* No Passives on this item */
        if (passives == null)
            return;

        Entity arrow = event.getProjectile();

        /* Apply to metadata so we can apply passive effects when the arrow hits. */
        arrow.setMetadata("passive", new FixedMetadataValue(kitPvP.getOrbitMines(), passives));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        /* Not an arrow, move on. */
        if (!(projectile instanceof Arrow))
            return;

        /* No passives, move on. */
        if (!projectile.hasMetadata("passive")) {
            /* Clear arrow so it doesn't just sit on the ground. */
            projectile.remove();
            return;
        }

        Map<Passive, Integer> passives = (Map<Passive, Integer>) projectile.getMetadata("passive").get(0).value();

        for (Passive passive : passives.keySet()) {
            passive.getHandler().trigger(event, passives.get(passive));
        }

        /* Clear arrow so it doesn't just sit on the ground. */
        projectile.remove();
    }
}
