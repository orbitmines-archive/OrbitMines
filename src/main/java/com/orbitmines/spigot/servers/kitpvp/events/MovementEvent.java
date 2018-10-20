package com.orbitmines.spigot.servers.kitpvp.events;

import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MovementEvent implements Listener {

    private ItemStackNms nms;

    public MovementEvent(KitPvP kitPvP){
        this.nms = kitPvP.getOrbitMines().getNms().customItem();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){

        Player player = event.getPlayer();

        KitPvPPlayer omp = KitPvPPlayer.getPlayer(player);

        if(omp.getSelectedKit() == null || omp.isSpectator()){
            return;
        }

        ItemStack item = player.getInventory().getBoots();

        if(item == null){
            return;
        }

        Map<Passive, Integer> passives = Passive.from(nms, item, Passive.Interaction.MOVEMENT);

        if(passives == null){
            return;
        }

        for(Passive passive : passives.keySet()){
            passive.getHandler().trigger(event, passives.get(passive));
        }
    }
}
