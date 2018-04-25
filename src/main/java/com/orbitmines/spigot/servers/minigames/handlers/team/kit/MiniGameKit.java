package com.orbitmines.spigot.servers.minigames.handlers.team.kit;

import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.utils.Rarity;

/**
 * Created by Robin on 4/8/2018.
 */
public abstract class MiniGameKit extends Kit {

    private MiniGameType type;
    private Rarity rarity;

    private boolean deFault;
    private int cost;

    /** CONSTRUCTORS */
    public MiniGameKit(MiniGameType type, Rarity rarity, String name, int cost){
        super(name);
        this.type = type;
        this.rarity = rarity;
        this.cost = cost;
        this.deFault = false;
        addItems();
    }

    public MiniGameKit(MiniGameType type, String name) {
        this(type, name, 0);
    }

    public MiniGameKit(MiniGameType type, String name, int cost){
        this(type, Rarity.NORMAL, name, cost);
    }

    public MiniGameKit(MiniGameType type, Rarity rarity, String name){
        this(type, rarity, name, 0);
    }

    /** KIT METHODS */
    protected abstract void addItems();

    protected abstract void initKit(MiniGamePlayer player);

    protected abstract void afterKitUsage(MiniGamePlayer player);

    public void giveKit(MiniGamePlayer player){
        this.setItems(player);
        initKit(player);
        player.useKit();
    }

    public void clearKit(MiniGamePlayer player){
        afterKitUsage(player);
        player.selectKit(null);
        player.clearFullInventory();
    }

    /** GETTERS */
    public MiniGameType getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getCost() {
        return cost;
    }

    public boolean isDefault(){
        return deFault;
    }

    /** SETTERS */
    public void setDefault(){
        this.deFault = true;
    }
}
