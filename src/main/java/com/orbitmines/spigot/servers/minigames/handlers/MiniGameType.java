package com.orbitmines.spigot.servers.minigames.handlers;

import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.gui.MiniGameGUI;
import com.orbitmines.spigot.servers.minigames.handlers.gui.SpectatorGUI;
import com.orbitmines.spigot.servers.minigames.handlers.phase.MiniGamePhase;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.handlers.team.kit.MiniGameKit;
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

    private List<MiniGameKit> kits;
    private List<Place> places;

    private Team.Settings settings;
    private MiniGameKit defaultKit;

    private MiniGameGUI miniGameGUI;

    private int maxPlayers;
    private int minPlayers;

    private String name;
    private String shortName;

    public MiniGameType(String name, String shortName, Team.Settings settings, int maxPlayers){
        this.name = name;
        this.shortName = shortName;
        this.phases = new HashMap<>();
        this.kits = new ArrayList<>();
        this.places = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.minPlayers = maxPlayers / 2;
        this.settings = settings;
        this.miniGameGUI = new MiniGameGUI(this);
    }

    /** ABSTRACT METHODS */
    protected abstract GameState getNextState(GameState state);

    public abstract SpectatorGUI getSpectatorGUI(MiniGamePlayer player);

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
        phases.get(miniGame.getState()).run(miniGame);
        if(miniGame.isChangingState()){
            if(miniGame.getState() == GameState.LOBBY){
                if(!canStart(miniGame.getPlayerCount())) {
                    miniGame.setState(GameState.LOBBY, false);
                    return;
                }
            }
            setPhase(miniGame, getNextState(miniGame.getState()));
        }
    }

    /** SETTERS */
    public void setPhase(MiniGame miniGame, GameState gameState){
        MiniGamePhase phase = phases.get(gameState);
        if(miniGame.isChangingState()){
            phase.onEnd(miniGame);

            GameState newState = getNextState(miniGame.getState());
            miniGame.setState(newState, true);

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

    public void addPlace(Place place){
        this.places.add(place);
    }

    public void addKit(MiniGameKit kit){
        this.kits.add(kit);
        if(kit.isDefault()){
            this.defaultKit = kit;
        }
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

    public List<MiniGameKit> getKits() {
        return kits;
    }

    public MiniGameKit getDefaultKit() {
        return defaultKit;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public MiniGameGUI getMiniGameGUI() {
        return miniGameGUI;
    }

    /** BOOLEANS */
    public boolean canJoin(int playercount){
        return playercount < maxPlayers;
    }

    private boolean canStart(int playercount){
        return playercount >= minPlayers;
    }

    public boolean hasDefaultKit(){
        return defaultKit != null;
    }

    public boolean hasKits(){
        return kits.size() > 0;
    }

    public boolean isMiniGame(String shortName){
        return this.shortName.equals(shortName);
    }
}
