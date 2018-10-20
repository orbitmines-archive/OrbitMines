package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class PassiveSpiderClimb implements Passive.Handler<PlayerMoveEvent> {

    @Override
    public void trigger(PlayerMoveEvent event, int level) {

        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();
        if(!(from.getX() == to.getX() && from.getZ() == to.getZ())) {
            if (!player.isSprinting()) {
                if (from.getX() == to.getX() || from.getZ() == to.getZ()) {
                    for(double i = 0; i < 0.1; i += 0.01){

                        /*
                        * TODO:
                        * -FIX DONT TELEPORTING WHEN UNDER A BLOCK
                        * -SMOOTH TELEPORTING WITH RUNNABLE!
                        *
                        * */








                        //TELEPORTING
                        Location newLoc = event.getPlayer().getLocation().add(0, i, 0);
                        newLoc.setYaw(to.getYaw());
                        newLoc.setPitch(to.getPitch());
                        player.teleport(newLoc);
                    }
                }
            }
        }
    }
}
