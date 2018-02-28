package com.orbitmines.spigot.servers.uhsurvival.events.UHEvents;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Robin on 2/28/2018.
 */
public class MobAttackEvent extends Event implements Cancellable {

    private boolean cancelled = false;

    private Mob attacker;

    private UHPlayer player_defender;
    private Mob mob_defender;

    private Entity entity_defender;

    private double attack;

    public MobAttackEvent(Mob attacker, UHPlayer player_defender, Mob mob_defender, Entity entity_defender){
        this.attacker = attacker;
        this.player_defender = player_defender;
        this.mob_defender = mob_defender;
        this.entity_defender = entity_defender;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public double getAttack() {
        return attack;
    }

    public Entity getEntity() {
        return entity_defender;
    }

    public Mob getAttacker() {
        return attacker;
    }

    public Mob getMob() {
        return mob_defender;
    }

    public UHPlayer getPlayer() {
        return player_defender;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }
}
