package com.orbitmines.spigot.servers.uhsurvival2.handlers.item;

import com.orbitmines.spigot.servers.uhsurvival2.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival2.handlers.UHPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemManager {

    private static UHSurvival uhSurvival;

    public ItemManager (UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
    }

    public static abstract class ClickAbleItem {

        private Material material;
        private byte data;

        public ClickAbleItem(Material material, byte data){
            this.material = material;
            this.data = data;
        }

        @Override
        public boolean equals(Object object){
            if(object instanceof ItemStack){
                ItemStack item = (ItemStack) object;
                if(item.getType() == material){
                    if(item.getData().getData() == data){
                        return equals(item);
                    }
                }
            }
            return false;
        }

        public abstract void click(UHPlayer player);

        public abstract ItemStack createItem();

        public UHSurvival getInstance(){
            return uhSurvival;
        }

        public abstract boolean equals(ItemStack item);
    }
}
