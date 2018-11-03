package com.orbitmines.spigot.servers.minigames.handlers.gui;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Robin on 4/23/2018.
 */
public class MiniGameGUI extends GUI {

    private MiniGameType type;

    public MiniGameGUI(MiniGameType type){
        this.type = type;
        createInventory();
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        return false;
    }

    private void createInventory(){
        for(MiniGame miniGame : MiniGamePlayer.getMiniGames(type)){
            ItemBuilder item;
            switch(miniGame.getState()){
                case LOBBY:
                    item = new ItemBuilder(Material.LIME_STAINED_GLASS);
                    break;
                case MAINTENANCE:
                    item = new ItemBuilder(Material.BLACK_STAINED_GLASS);
                    break;
                default:
                    item = new ItemBuilder(Material.RED_STAINED_GLASS);
                    break;
            }

            item.setDisplayName(" " + type.getName() + " #" + miniGame.getId() + ""); //TODO: ADD COLORS!

            List<String> lore = new ArrayList<>();

            lore.add("--------------------");
            lore.add("       " + miniGame.getState().toString().toLowerCase() + "       ");
            lore.add("    " + miniGame.getTime() / 60 + " min " + miniGame.getTime() % 60 + " sec    ");
            lore.add("       " + miniGame.getPlayerCount() + " / " + type.getMaxPlayers() + "       ");
            lore.add("--------------------");

            item.setLore(lore);

            this.add(miniGame.getId(), new MiniGameInstance(miniGame, item.build()));

            /*   ChickenFight #1
              --------------------
            *        Lobby
            *     10 min 1 sec
            *        0 / 16
            * --------------------
            * */
        }
    }

    public void update(){
        for(ItemInstance instance : this.itemInstances){
            MiniGameInstance item = (MiniGameInstance) instance;
            item.update();
        }
    }

    public class MiniGameInstance extends ItemInstance {

        private MiniGame miniGame;

        MiniGameInstance(MiniGame miniGame, ItemStack itemStack) {
            super(itemStack);
            this.miniGame = miniGame;
        }

        @Override
        public void onClick(InventoryClickEvent event, OMPlayer omp) {
            omp.connect(Server.MINIGAMES, false);
            MiniGamePlayer player = (MiniGamePlayer) omp;
            if(!player.isInGame()){
                player.join(miniGame);
            }
        }

        public void update(){
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = getItemStack().getItemMeta().getLore();
            lore.set(1, "       " + miniGame.getState().toString().toLowerCase() + "       ");
            lore.set(2, "    " + miniGame.getTime() / 60 + " min " + miniGame.getTime() % 60 + " sec    ");
            lore.set(3, "       " + miniGame.getPlayerCount() + " / " + type.getMaxPlayers() + "       ");
            if(miniGame.isChangingState()) {
                switch (miniGame.getState()) {
                    case LOBBY:
                        this.getItemStack().setDurability((short) 5);
                        break;
                    case MAINTENANCE:
                        this.getItemStack().setDurability((short) 15);
                        break;
                    default:
                        this.getItemStack().setDurability((short) 14);
                        break;
                }
            }
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
    }
}
