package com.orbitmines.spigot.servers.uhsurvival.events;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.FoodManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Robin on 3/8/2018.
 */
public class FoodEvents implements Listener {

    private UHSurvival uhSurvival;

    public FoodEvents(UHSurvival uhSurvival){
        this.uhSurvival = uhSurvival;
    }

    @EventHandler
    public void onFoodEat(PlayerItemConsumeEvent event){
        UHPlayer uhPlayer = UHPlayer.getUHPlayer(event.getPlayer());
        if(uhPlayer != null){
            ItemStack item = event.getItem();
            uhSurvival.getFoodManager().consume(uhPlayer, item.getType(), item.getData().getData());
        }
    }

    @EventHandler
    public void onFoodChangeLevel(FoodLevelChangeEvent event){
        if(event.getEntity() instanceof Player){
            UHPlayer p = UHPlayer.getUHPlayer(event.getEntity().getUniqueId());
            if(p != null){
                FoodManager.Food food = p.getProfile().getLastEatenFood();
                if(food != null){
                    float newSaturation = p.getPlayer().getSaturation() + food.getSaturation(p);
                    int newFoodLevel = MathUtils.clamp(p.getPlayer().getFoodLevel() + food.getFoodLevel(p), 0, 20);
                    p.getPlayer().setSaturation(newSaturation);
                    p.getPlayer().setFoodLevel(newFoodLevel);
                    p.getProfile().setLastFood(null);
                    event.setCancelled(true);
                }
            }
        }
    }
}