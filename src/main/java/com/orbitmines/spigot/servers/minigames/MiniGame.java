package com.orbitmines.spigot.servers.minigames;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.handlers.team.Team;
import com.orbitmines.spigot.servers.minigames.utils.GameState;

import java.util.HashSet;
import java.util.Set;

import static com.orbitmines.spigot.servers.minigames.utils.GameState.*;

/**
 * Created by Robin on 3/27/2018.
 */
public class MiniGame {

    private Kit spectatorKit;

    private Set<Team> teams;
    private Set<MiniGamePlayer> players;
    private Set<MiniGamePlayer> spectators;

    private GameState currentState;

    private MiniGameType type;

    private OrbitMinesMap map;

    private int time;
    private int id;

    private boolean stopped;

    public MiniGame(MiniGameType type, int id){
        this.type = type;
        this.teams = new HashSet<>();
        this.players = new HashSet<>();
        this.spectators = new HashSet<>();
        this.currentState = GameState.RESTARTING;
        this.id = id;
        this.stopped = false;
    }

    /** TIME MECHANISM */
    public void run(){
        this.type.run(this);
        this.time--;
    }

    public boolean isChangingState(){
        return time == 0;
    }

    public void reset(){
        if(currentState != RESTARTING) setState(RESTARTING, true);
        for(MiniGamePlayer player : players){
            player.leave();
        }
        for(MiniGamePlayer player : spectators){
            player.leave();
        }
        teams.clear();
        players.clear();
        spectators.clear();
        stopped = false;
        map = null;
    }

    /** GETTERS */
    public Set<Team> getTeams() {
        return teams;
    }

    public Set<MiniGamePlayer> getPlayers() {
        return players;
    }

    public MiniGameType getType() {
        return type;
    }

    public GameState getState(){
        return currentState;
    }

    public OrbitMinesMap getMap() {
        return map;
    }

    public Team getTeam(MiniGamePlayer player){
        for(Team team : teams){
            if(team.contains(player)){
                return team;
            }
        }
        return null;
    }

    public int getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public int getPlayerCount(){
        return players.size();
    }

    /** SETTERS */
    public void setState(GameState state, boolean renewState){
        this.currentState = state;
        this.time = type.getTime(state);
        if(renewState) {
            this.type.setPhase(this, state);
        }
    }

    public void generateMap(){ //TODO!
        this.map = OrbitMinesMap.getRandomMap(Server.MINIGAMES, map);
    }

    public void stop(){
        this.stopped = true;
    }

    /** BOOLEANS */
    public boolean isSpectator(MiniGamePlayer player){
        return spectators.contains(player);
    }

    public boolean canJoin(){
        return (currentState == RUNNING) || (currentState == LOBBY && type.canJoin(players.size()));
    }

    public boolean hasStopped(){
        return stopped;
    }

    /** PLAYER METHODS */
    public void broadcast(String message){
        for(Team team : teams){
            for(MiniGamePlayer player : team.getTeamMates()){
                player.sendMessage(message);
            }
        }
    }

    public void say(MiniGamePlayer player, String message){
        if(isSpectator(player)){
            for(MiniGamePlayer p : spectators){
                p.sendMessage(message);
            }
        } else {
            broadcast(message);
        }
    }

    public void join(MiniGamePlayer player){
        if(currentState == RUNNING){
            this.spectators.add(player);
        } else if(currentState == LOBBY){
            this.players.add(player);
        }
        if(type.hasDefaultKit()){
            player.selectKit(type.getDefaultKit());
        }
        broadcast("JOINING MESSAGE");
    }

    public void leave(MiniGamePlayer player){
        if(currentState == RUNNING){
            Team team  = getTeam(player);
            if(!team.isDead(player)){
                team.die(player);
            } else {
                spectators.remove(player);
            }
        } else {
            players.remove(player);
        }
        broadcast("LEAVING MESSAGE");
    }

    /**  */
    public void updateSpectators(){
        for(MiniGamePlayer player : spectators){
            //TODO
        }
    }
}
