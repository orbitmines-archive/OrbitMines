package com.orbitmines.spigot.servers.uhsurvival.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.ToolInventory;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments.Enchantments;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.Map;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;
import com.orbitmines.spigot.servers.uhsurvival.utils.Enchantment;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class UHPlayer extends OMPlayer implements Attacker {

    private static HashMap<UUID, UHPlayer> players = new HashMap<>();

    private MapSection mapSection;
    private ToolInventory inventory;

    private UHSurvival instance;

    public UHPlayer(UHSurvival uhSurvival, Player player) {
        super(player);
        players.put(getUUID(), this);
        this.instance = uhSurvival;
        this.updateMapLocation();
        this.inventory = new ToolInventory(getInventory());
    }

    @Override
    public Collection<ComponentMessage.TempTextComponent> getChatPrefix() {
        return null;
    }

    /* MAP METHODS */
    public MapSection getMapLocation() {
        return mapSection;
    }

    public void updateMapLocation(){
        if(this.mapSection != null) {
            this.mapSection.removePlayer(this);
        }
        Map map = instance.getMap(this.getWorld());
        if(map != null){
            this.mapSection = map.getMapSection(this.getLocation());
            this.mapSection.addPlayer(this);
        }
    }

    /* OM-PLAYER METHODS */
    @Override
    protected void onLogin() {

    }

    @Override
    protected void onLogout() {

    }

    @Override
    protected void onFirstLogin() {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /* ATTACKER METHODS TODO!*/
    @Override
    public boolean attack(Attacker defender) {
        if (defender instanceof UHPlayer) {
            UHPlayer p = (UHPlayer) defender;
            if (p.getMapLocation().canPvP() && getMapLocation().canPvP()) {
                return true;
            }
        }
        boolean cancelled = defender.defend(this);
        if (!cancelled) {
            Tool mainHand = getToolInventory().getMainHand();
            if (mainHand != null) {
                if (defender instanceof Mob) {
                    Mob mob = (Mob) defender;
                    if (mob.hasMobType()) {
                        mainHand.addExp(mob.getType().getItemExp());
                    }
                }
                for (Enchantment enchantment : mainHand.getEnchantments().keySet()) {
                    if(!cancelled && enchantment.getOutput() instanceof Enchantments.AttackEnchantment) {
                        if(MathUtils.randomize(0, 100, (int) enchantment.getChance()))
                        cancelled = ((Enchantments.AttackEnchantment) enchantment.getOutput()).output(this, defender, mainHand.getLevel(enchantment));
                    }
                }
            }
        }
        return cancelled;
    }

    @Override
    public boolean defend(Attacker attacker) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for(Tool tool : getToolInventory().getArmor()){
            if(tool != null){
                if(attacker instanceof Mob){
                    Mob mob = (Mob) attacker;
                    if(mob.hasMobType()){
                        tool.addExp(mob.getType().getArmorExp());
                    }
                }
                for(Enchantment enchantment : tool.getEnchantments().keySet()){
                    if(enchantments.containsKey(enchantment)){
                        enchantments.put(enchantment, tool.getLevel(enchantment) + enchantments.get(enchantment));
                    } else {
                        enchantments.put(enchantment, tool.getLevel(enchantment));
                    }
                }
            }
        }
        boolean cancelled = false;
        for(Enchantment enchantment : enchantments.keySet()){
            if(cancelled) break;
            if(enchantment.getOutput() instanceof Enchantments.AttackEnchantment) {
                if (MathUtils.randomize(0, 100, (int) enchantment.getChance())) {
                    cancelled = ((Enchantments.AttackEnchantment) enchantment.getOutput()).output(attacker, this, enchantments.get(enchantment));
                }
            }
        }
        return cancelled;
    }

    @Override
    public ToolInventory getToolInventory() {
        return inventory;
    }

    @Override
    public boolean hasInventory() {
        return true;
    }

    /* STATIC METHODS */
    public static UHPlayer getUHPlayer(UUID id){
        return players.get(id);
    }

    public static UHPlayer getUHPlayer(Player player){
        return getUHPlayer(player.getUniqueId());
    }

    public static Collection<UHPlayer> getUHPlayers(){
        return players.values();
    }
}
