package com.orbitmines.spigot.servers.minigames.handlers;

import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.phase.MiniGamePhase;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.handlers.team.place.Place;
import com.orbitmines.spigot.servers.minigames.utils.GameState;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Robin on 3/27/2018.
 */
public abstract class MiniGameType {

    private Map<GameState, MiniGamePhase> phases;

    private List<Place> places;

    private Team.Settings settings;

    private int maxPlayers;
    private int minPlayers;

    private String name;

    public MiniGameType(String name, Team.Settings settings, int maxPlayers){
        this.name = name;
        this.phases = new HashMap<>();
        this.places = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.minPlayers = maxPlayers / 2;
        this.settings = settings;
    }

    /** ABSTRACT METHODS */
    protected abstract GameState getNextState(GameState state);

    /** RUN METHODS */
    public void run(MiniGame miniGame){
        String starting = miniGame.getState() == GameState.RUNNING ? "ending" : miniGame.getState() == GameState.LOBBY ? "starting" : null;
        if(starting != null) {
            if (miniGame.getTime() % 60 == 0) {
                miniGame.broadcast(starting + " in " + miniGame.getTime() / 60 + " minute(s)");
            } else if (miniGame.getTime() < 10) {
                miniGame.broadcast(starting + " in " + miniGame.getTime() + " second(es)");
                for(MiniGamePlayer player : miniGame.getPlayers()){
                    player.playSound(Sound.UI_BUTTON_CLICK);
                }
            }
        }

        if(miniGame.isChangingState()){
            setPhase(miniGame, getNextState(miniGame.getState()));
        }
        phases.get(miniGame.getState()).run(miniGame);
    }

    /** SETTERS */
    public void setPhase(MiniGame miniGame, GameState gameState){
        MiniGamePhase phase = phases.get(gameState);
        if(miniGame.isChangingState()){
            phase.onEnd(miniGame);

            GameState newState = getNextState(miniGame.getState());
            miniGame.setState(newState);

            MiniGamePhase newPhase = phases.get(newState);
            newPhase.start(miniGame);

            for(MiniGamePlayer player : miniGame.getPlayers()){
                player.clearScoreboard();
                player.setScoreboard(newPhase.getScoreboard(player));
            }
        }
    }

    public void addPhase(GameState state, MiniGamePhase phase){
        this.phases.put(state, phase);
    }

    /** GETTERS */
    public List<Place> getPlaces() {
        return places;
    }

    public int getTime(GameState state){
        return phases.get(state).getDuration();
    }

    public String getName() {
        return name;
    }

    public Team.Settings getSettings() {
        return settings;
    }

    public Place getPlace(int place){
        for(Place p : places){
            if(p.getPlace() == place){
                return p;
            }
        }
        return null;
    }

    /** BOOLEANS */
    public boolean canJoin(int playercount){
        return playercount < maxPlayers;
    }

    public boolean canStart(int playercount){
        return playercount >= minPlayers;
    }
}
