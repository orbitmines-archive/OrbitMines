package com.orbitmines.spigot.servers.minigames.minigames.cf;

import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.gui.SpectatorGUI;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.handlers.team.place.Place;
import com.orbitmines.spigot.servers.minigames.minigames.cf.phases.ChickenFightRunningPhase;
import com.orbitmines.spigot.servers.minigames.utils.GameState;
import org.bukkit.entity.Player;

/**
 * Created by Robin on 4/5/2018.
 */
public class ChickenFight extends MiniGameType {

    public ChickenFight() {
        super("ChickenFight", "MG_CF",

                new Team.Settings(1, false, false, false),
                12);
        //ADDING PHASES
        this.addPhase(GameState.RUNNING, new ChickenFightRunningPhase(this));


        //ADDING PLACES
    }

    @Override
    public MiniGamePlayer getPlayer(Player player) {
        return null;
    }

    @Override
    protected GameState getNextState(GameState state) {
        switch(state){
            case LOBBY:
                return GameState.WARM_UP;
            case WARM_UP:
                return GameState.RUNNING;
            case RUNNING:
                return GameState.ENDING;
            case ENDING:
                return GameState.RESTARTING;
            case RESTARTING:
                return GameState.LOBBY;
            default:
                return GameState.MAINTENANCE;
        }
    }

    @Override
    public SpectatorGUI getSpectatorGUI(MiniGamePlayer player) {
        return null;
    }

    /** PLACES */
    private class FirstPlace extends Place {

        FirstPlace() {
            super(Place.FIRST);
        }

        @Override
        public void gainReward(MiniGamePlayer player) {

        }
    }

    /** KITS */
}
