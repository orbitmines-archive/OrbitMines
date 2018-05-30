package com.orbitmines.spigot.servers.uhsurvival.handlers.mob;

import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.ToolInventory;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Action;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Robin on 2/28/2018.
 */
public class Mob {

    private int level;

    private World world;
    private MapSection section;

    private MobType type;

    private Entity entity;

    private ToolInventory inv;

    private UHPlayer target;
    private UHPlayer killer;

    public Mob(MobType type, Entity entity, int level){
        this.type = type;
        this.entity = entity;
        this.level = level;
        this.world = World.getWorldByEnvironment(entity.getWorld().getEnvironment());
        this.section = world.getMap().getMapSection(entity.getLocation());
        if(type.getType().canHoldItems() || type.getType().canWearArmor()) {
            this.inv = new ToolInventory(((LivingEntity)entity).getEquipment());
        }
        this.target = null;
    }

    /* STANDARD METHODS */
    public void death(){
        this.type.death(this);
        if(entity instanceof LivingEntity){
            if(((LivingEntity) entity).getKiller() != null){
                UHPlayer p = UHPlayer.getUHPlayer(((LivingEntity) entity).getKiller().getUniqueId());
                if(p != null){
                    setKiller(p);
                }
            }
        }
    }

    public void spawn(){
        this.type.spawn(this);
    }

    public boolean protect(UHSurvival uhSurvival, Event event){
        Tool[] armor = inv.getArmor();
        HashMap<Enchantment, Integer> enchantment = new HashMap<>();
        for(Tool piece : armor){
            if(piece != null){
                for(Enchantment e : piece.getEnchantments().keySet()){
                    if(!enchantment.containsKey(e)) {
                        enchantment.put(e, piece.getEnchantment(e));
                    } else {
                        int level = piece.getEnchantment(e) > enchantment.get(e) ? piece.getEnchantment(e) : enchantment.get(e);
                        enchantment.put(e, level);
                    }
                }
            }
        }
        this.updateMap();
        uhSurvival.getEnchantmentManager().output(enchantment, Action.PROTECT, event, false);
        return false;
    }

    public void shoot(UHSurvival uhSurvival, EntityShootBowEvent event) {
        Tool bow = null;
        ItemStack b = event.getBow();
        if (getInventory().getMainHand().equals(b)) {
            bow = getInventory().getMainHand();
        } else if (getInventory().getOffHand().equals(b)) {
            bow = getInventory().getOffHand();
        }
        if (bow != null) {
            uhSurvival.getEnchantmentManager().output(bow.getEnchantments(), Action.SHOOT, event, true);
        }
        updateMap();
    }

    /* GETTERS */
    public UHPlayer getTarget() {
        return target;
    }

    public boolean hasTarget(){
        return target != null;
    }

    public ToolInventory getInventory() {
        return inv;
    }

    public Entity getEntity() {
        return entity;
    }

    public UHPlayer getKiller() {
        return killer;
    }

    public MobType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public MapSection getSection() {
        return section;
    }

    public boolean isInRange(Location location){
        return location.distance(this.entity.getLocation()) < this.getType().getRadius();
    }

    public void updateMap(){
        MapSection newSection = world.getMap().getMapSection(entity.getLocation());
        if(newSection != null && newSection != section){
            section.removeMob(this.entity);
            newSection.addMob(this);
            section = newSection;
        }
    }

    /* SETTERS */
    public void setTarget(UHPlayer target) {
        this.target = target;
        if(entity instanceof Creature){
            ((Creature) entity).setTarget(target.getPlayer());
        }
    }

    public void setKiller(UHPlayer killer) {
        this.killer = killer;
    }
}
