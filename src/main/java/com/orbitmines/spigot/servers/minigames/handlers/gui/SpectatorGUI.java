package com.orbitmines.spigot.servers.minigames.handlers.gui;

import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Robin on 4/23/2018.
 */
public abstract class SpectatorGUI extends GUI {

    private MiniGame miniGame;
    private MiniGamePlayer spectator;

    public SpectatorGUI(MiniGamePlayer player){
        this.spectator = player;
        this.miniGame = player.getGame();
    }

    public void update(){
        for(ItemInstance instance : itemInstances){
            SpectatorItem item = (SpectatorItem) instance;
            if(item.getPlayer().isSpectator()){

            }
            item.update();
        }
    }

    public MiniGamePlayer getSpectator() {
        return spectator;
    }

    public MiniGame getMiniGame() {
        return miniGame;
    }

    public abstract void createInventory();

    public abstract class SpectatorItem extends ItemInstance {

        private MiniGamePlayer player;

        public SpectatorItem(MiniGamePlayer player, ItemStack itemStack) {
            super(itemStack);
            this.player = player;
        }

        @Override
        public void onClick(InventoryClickEvent event, OMPlayer omp) {
            omp.getPlayer().teleport(player.getPlayer());
        }

        public abstract void update();

        public MiniGamePlayer getPlayer() {
            return player;
        }
    }
}
