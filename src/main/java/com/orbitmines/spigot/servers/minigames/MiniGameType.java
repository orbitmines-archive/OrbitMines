package com.orbitmines.spigot.servers.minigames;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.Team;
import com.orbitmines.spigot.servers.minigames.handlers.stats.Place;
import com.orbitmines.spigot.servers.minigames.utils.GameState;

import java.util.*;

/**
 * Created by Robin on 3/18/2018.
 */
public abstract class MiniGameType {

    private static Set<OrbitMinesMap> lobbies = new HashSet<>();
    private static PreventionSet preventLobbies = new PreventionSet();

    private Set<PreventionSet.Prevention> preventions;
    private Set<Kit> kits;
    private Set<OrbitMinesMap> map;

    private List<Place> places;

    private Team.Settings settings;

    private PreventionSet preventionSet = new PreventionSet();

    private HashMap<GameState, Integer> gameStates;

    private int maxPlayers;
    private int minPlayers;

    private String name;

    public MiniGameType(String name, int maxPlayers, Team.Settings settings, PreventionSet.Prevention... preventions){
        this.name = name;
        this.kits = new HashSet<>();
        this.map = new HashSet<>();
        this.gameStates = new HashMap<>();
        this.preventions = new HashSet<>();
        this.preventions.addAll(Arrays.asList(preventions));
        this.settings = settings;
        this.places = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.minPlayers = maxPlayers / 2;
    }

    /* STATE METHODS */
    abstract void warmUp(MiniGame miniGame);

    abstract void lobby(MiniGame miniGame);

    abstract void running(MiniGame miniGame);

    abstract HashMap<MiniGamePlayer, Integer> ending(MiniGame miniGame);

    abstract void restarting(MiniGame miniGame);

    /* If extra game-states may be added! */
    void runGameState(MiniGame miniGame){}

    abstract GameState getNextState(GameState state);

    protected void run(MiniGame miniGame, boolean first){
        switch(miniGame.getState()){
            case LOBBY:
                if(miniGame.hasLobby()) {
                    lobby(miniGame);
                    if (first) {
                        miniGame.setMap(getMap());
                    }
                } else {
                    miniGame.reset();
                    //TODO: SEND MESSAGE SOMETHING WENT WRONG
                }
                break;
            case WARM_UP:
                warmUp(miniGame);
                if(first) {
                    if(miniGame.hasMap()) {
                        //TODO: FIGURE OUT HOW THE DATAPOINT THINGY WORKS XD
                    } else {
                        //TODO: SENDMESSAGE SOMETHING WENT WRONG!!
                        miniGame.reset();
                    }
                }
                break;
            case RUNNING:
                running(miniGame);
                break;
            case ENDING:
                HashMap<MiniGamePlayer, Integer> places = ending(miniGame);
                if(first) {
                    for (MiniGamePlayer player : places.keySet()) {
                        int place = places.get(player);
                        for (Place p : this.places) {
                            if (p.getPlace() == place) {
                                p.gainReward(player);
                                player.getData(this).addWin();
                            }
                        }
                    }
                    for (MiniGamePlayer player : miniGame.getDeadPlayers()) {
                        player.getData(this).addLose();
                    }
                }
                break;
            case RESTARTING:
                restarting(miniGame);
                if(first) {
                    for(MiniGamePlayer player : miniGame.getSpectators()){
                        player.leave();
                    }
                    for(MiniGamePlayer player : miniGame.getPlayers()){
                        player.leave();
                    }
                    miniGame.setLobby(getLobby());
                }
                break;
            default:
                runGameState(miniGame);
                break;
        }
        miniGame.tick();
        if(miniGame.getTime() == 0){
            GameState state = getNextState(miniGame.getState());
            if(state != null){
                if(miniGame.getState() == GameState.LOBBY){
                    if(canStart(miniGame.getPlayercount())){
                        int time = gameStates.get(state);
                        miniGame.setTime(time);
                        miniGame.setState(state);
                    }
                } else {
                    int time = gameStates.get(state);
                    miniGame.setTime(time);
                    miniGame.setState(state);
                }
            }
        }
    }

    /* SETTERS */
    public void addMap(String map, WorldLoader.Type type, String authors){
        OrbitMinesMap omMap = new OrbitMinesMap("minigame_" + name + "_" + map, type, map, OrbitMinesMap.Type.GAMEMAP, Server.MINIGAMES, false, authors);
        this.map.add(omMap);
        this.preventionSet.prevent(omMap.getWorld(), (PreventionSet.Prevention[]) preventions.toArray());
    }

    public void addKit(Kit kit){
        this.kits.add(kit);
    }

    public void addGameState(GameState gameState, int time){
        this.gameStates.put(gameState, time);
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    /* GETTERS */
    public Set<Kit> getKits() {
        return kits;
    }

    public OrbitMinesMap getMap(){
        for(OrbitMinesMap map : map){
            if(!map.isEnabled()){
                return map;
            }
        }
        return null;
    }

    public Set<OrbitMinesMap> getMaps() {
        return map;
    }

    public String getName() {
        return name;
    }

    int getTime(GameState state){
        return gameStates.get(state);
    }

    /* LOBBY METHODS */
    public static Set<OrbitMinesMap> getLobbies() {
        return lobbies;
    }

    public static void addLobby(OrbitMinesMap lobby){
        lobbies.add(lobby);
        preventLobbies.prevent(lobby.getWorld(), PreventionSet.Prevention.values());
    }

    private OrbitMinesMap getLobby(){
        for(OrbitMinesMap map : lobbies){
            if(!map.isEnabled()){
                return map;
            }
        }
        return null;
    }

    /* PLAYER METHODS */
    boolean canJoin(int players){
        return players < maxPlayers;
    }

    private boolean canStart(int players){
        return players >= minPlayers;
    }
}
