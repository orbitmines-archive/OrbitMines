package com.orbitmines.spigot.servers.minigames.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.MiniGames;
import com.orbitmines.spigot.servers.minigames.handlers.gui.SpectatorGUI;
import com.orbitmines.spigot.servers.minigames.handlers.stats.Stats;
import com.orbitmines.spigot.servers.minigames.handlers.team.kit.MiniGameKit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robin on 3/27/2018.
 */
public class MiniGamePlayer extends OMPlayer {

    private static MiniGames miniGames;

    private MiniGame miniGame;

    private List<Stats.Data> stats;

    private HashMap<MiniGameKit, Integer> usesKits;

    private MiniGameKit selectedKit;

    private SpectatorGUI spectatorGUI;

    private boolean spectator;

    public MiniGamePlayer(MiniGames miniGames, Player player) {
        super(player);
        this.miniGames = miniGames;
        this.stats = new ArrayList<>();
        this.spectator = false;
        this.usesKits = new HashMap<>();
    }

    @Override
    protected void onLogin() {

    }

    @Override
    protected void onLogout() {

    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /**
     * MINI-GAME METHODS
     */
    public boolean isInGame() {
        return miniGame != null;
    }

    public void join(MiniGame miniGame) {
        this.miniGame = miniGame;
    }

    public MiniGame getGame() {
        return miniGame;
    }

    public void leave() {
        this.miniGame = null;
    }

    /**
     * SPECTATOR METHODS
     */
    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
        if (spectator) {
            this.spectatorGUI = miniGame.getType().getSpectatorGUI(this);
        }
    }

    public boolean hasSpectatorGUI() {
        return spectatorGUI != null;
    }

    public SpectatorGUI getSpectatorGUI() {
        return spectatorGUI;
    }

    /**
     * KIT METHODS
     */
    public MiniGameKit getDefaultKit() {
        return miniGame != null ? miniGame.getType().getDefaultKit() : null;
    }

    public void selectKit(MiniGameKit kit) {
        this.selectedKit = kit;
    }

    public MiniGameKit getSelectedKit() {
        return selectedKit;
    }

    public boolean hasSelected() {
        return selectedKit != null;
    }

    public boolean canSelect(MiniGameKit kit) {
        return usesKits.containsKey(kit) && usesKits.get(kit) > 0;
    }

    public void addUses(MiniGameKit kit, int uses) {
        if (usesKits.containsKey(kit)) {
            this.usesKits.put(kit, usesKits.get(kit) + uses);
        } else {
            this.usesKits.put(kit, uses);
        }
    }

    public void useKit() {
        this.usesKits.put(selectedKit, usesKits.get(selectedKit) - 1);
    }

    /**
     * STATS METHODS
     */
    public Stats.Data getData(MiniGameType type) {
        for (Stats.Data data : stats) {
            if (data.getStats().getType() == type) {
                return data;
            }
        }
        return null;
    }

    public static List<MiniGame> getMiniGames(MiniGameType type) {
        return miniGames.getMiniGames(type);
    }
}
