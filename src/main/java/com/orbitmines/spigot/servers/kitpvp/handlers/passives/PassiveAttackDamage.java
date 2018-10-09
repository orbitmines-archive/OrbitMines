package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.nms.entity.EntityNms;

public class PassiveAttackDamage implements Passive.Handler<DummyEvent> {

    @Override
    public void trigger(DummyEvent event, int level) {
       event.getKitPvP().getOrbitMines().getNms().entity().setAttribute(event.getPlayer().getPlayer(), EntityNms.Attribute.ATTACK_DAMAGE, level);
    }
}
