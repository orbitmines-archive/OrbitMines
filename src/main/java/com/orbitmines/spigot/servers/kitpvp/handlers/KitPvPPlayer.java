package com.orbitmines.spigot.servers.kitpvp.handlers;

import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitPvPPlayer extends OMPlayer {

    private static ArrayList<KitPvPPlayer> players = new ArrayList<>();

    private final LevelData levelData;

    private KitPvPKit.Level selectedKit;

    public KitPvPPlayer(Player player) {
        super(player);

        levelData = new LevelData(this);
    }

    @Override
    protected void onLogin() {
        players.add(this);

        clearFullInventory();
        clearPotionEffects();

        /* Force Update */
        getData();

        /* Update Level */
        levelData.update(false);

        /* Set Scoreboard */
        setScoreboard(new KitPvP.Scoreboard(orbitMines, this));
    }

    @Override
    protected void onLogout() {


        players.remove(this);
    }

    @Override
    protected void onFirstLogin() {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;//TODO
    }

    public KitPvPKit.Level getSelectedKit() {
        return selectedKit;
    }

    public LevelData getLevelData() {
        return levelData;
    }

    /*
        Coins
     */

    public int getCoins() {
        return getData().getCoins();
    }

    public void addCoins(int amount, boolean notify) {
        if (notify)
            new ActionBar(this, () -> "§6+" + amount + " " + (amount == 1 ? "Coin" : "Coins"), 100).send();

        getData().addCoins(amount);
    }

    public void removeCoins(int amount) {
        getData().removeCoins(amount);
    }

    /*
        Experience
     */

    public int getExperience() {
        return getData().getExperience();
    }

    public void addExperience(int amount, boolean notify) {
        if (levelData.getLevel() == LevelData.maxLevel)
            return;

        if (notify)
            new ActionBar(this, () -> "§e+" + amount + " XP", 100).send();

        getData().addExperience(amount);

        levelData.update(true);
    }

    public void addCoinsExperience(int coins, int experience, boolean notify) {
        if (notify)
            new ActionBar(this, () -> "§6+" + coins + " " + (coins == 1 ? "Coin" : "Coins") + "§7, " + "§e+" + experience + " XP", 100).send();

        addCoins(coins, false);
        addExperience(experience, false);
    }

    /*
        Kills
     */

    public int getKills() {
        return getData().getKills();
    }

    public void addKill() {
        getData().addKill(selectedKit.getHandler().getId());
    }

    /*
        Deaths
     */

    public int getDeaths() {
        return getData().getDeaths();
    }

    public void addDeath() {
        getData().addDeath(selectedKit.getHandler().getId());
    }

    /*
        KitData
     */

    public KitPvPData.KitData getKitData(KitPvPKit kit) {
        return getData().getKitData(kit.getId());
    }

    /* Personal Multiplier */
    public double getCoinMultiplier() {
        switch (vipRank) {

            case NONE:
            case IRON:
            case GOLD:
                return 1.0;
            case DIAMOND:
                return 1.2;
            case EMERALD:
                return 1.5;
        }
        throw new IllegalStateException();
    }

    public double getXPMultiplier() {
        switch (vipRank) {

            case NONE:
            case IRON:
                return 1.0;
            case GOLD:
                return 1.5;
            case DIAMOND:
                return 2.0;
            case EMERALD:
                return 2.5;
        }
        throw new IllegalStateException();
    }

    private KitPvPData getData() {
        return (KitPvPData) getData(Data.Type.KITPVP);
    }

    public static KitPvPPlayer getPlayer(Player player) {
        for (KitPvPPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        return null;
    }

    public static KitPvPPlayer getPlayer(String name) {
        for (KitPvPPlayer omp : players) {
            if (omp.getName(true).equalsIgnoreCase(name))
                return omp;
        }
        return null;
    }

    public static KitPvPPlayer getPlayer(UUID uuid) {
        for (KitPvPPlayer omp : players) {
            if (omp.getUUID().toString().equals(uuid.toString()))
                return omp;
        }
        return null;
    }

    public static List<KitPvPPlayer> getKitPvPPlayers() {
        return players;
    }
}
