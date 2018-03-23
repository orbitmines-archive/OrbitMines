package com.orbitmines.spigot.servers.minigames;

import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.Team;
import com.orbitmines.spigot.servers.minigames.utils.GameState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robin on 3/19/2018.
 */
public class MiniGame {

    private Set<Team> teamPlayers;

    private Set<MiniGamePlayer> players;
    private Set<MiniGamePlayer> deadPlayers;
    private Set<MiniGamePlayer> spectators;

    private HashMap<MiniGamePlayer, Kit> selectedKits;

    private OrbitMinesMap currentMap;
    private OrbitMinesMap currentLobby;

    private MiniGameType type;

    private GameState gameState;

    private int playercount;
    private int time;
    private int id;

    private boolean first;

    public MiniGame(MiniGameType miniGameType, int id){
        this.type = miniGameType;
        this.teamPlayers = new HashSet<>();
        this.players = new HashSet<>();
        this.deadPlayers = new HashSet<>();
        this.spectators = new HashSet<>();
        this.selectedKits = new HashMap<>();
        this.id = id;
        this.gameState = GameState.RESTARTING;
        this.time = type.getTime(gameState);
    }

    /* TIME MECHANISM */
    public void tick(){
        this.time--;
    }

    public void run(){
        type.run(this, first);
        this.first = false;
    }

    public void reset(){
        for(MiniGamePlayer player : players){
            player.leave();
        }
        for(MiniGamePlayer player : spectators){
            player.leave();
        }
    }

    /* GETTERS */
    public GameState getState() {
        return gameState;
    }

    public OrbitMinesMap getCurrentMap() {
        return currentMap;
    }

    public int getTime(){
        return time;
    }

    public int getId() {
        return id;
    }

    public OrbitMinesMap getLobby() {
        return currentLobby;
    }

    public Set<MiniGamePlayer> getDeadPlayers() {
        return deadPlayers;
    }

    public Set<MiniGamePlayer> getPlayers() {
        return players;
    }

    public Set<MiniGamePlayer> getSpectators() {
        return spectators;
    }

    public Set<Team> getTeams() {
        return teamPlayers;
    }

    public MiniGameType getType() {
        return type;
    }

    public HashMap<MiniGamePlayer, Kit> getSelectedKits() {
        return selectedKits;
    }

    public int getPlayercount() {
        return playercount;
    }

    /* SETTERS */
    protected void setLobby(OrbitMinesMap currentLobby) {
        this.currentLobby = currentLobby;
    }

    protected void setTime(int time){
        this.time = time;
    }

    protected void setState(GameState gameState) {
        this.gameState = gameState;
        this.first = true;
    }

    protected void setMap(OrbitMinesMap map){
        this.currentMap = map;
    }

    /* BOOLEANS */
    public boolean hasLobby(){
        return currentLobby != null;
    }

    public boolean hasMap(){
        return currentMap != null;
    }

    public boolean isSpectator(MiniGamePlayer player){
        return spectators.contains(player);
    }

    public boolean canJoin(){
        return (gameState == GameState.LOBBY && type.canJoin(playercount)) || gameState == GameState.RUNNING;
    }

    /* PLAYER METHODS */
    public void leave(MiniGamePlayer player){
        if(!isSpectator(player)){
            deadPlayers.add(player);
        } else {
            spectators.remove(player);
        }
        updatePlayerCount();
    }

    public void join(MiniGamePlayer player){
        if(gameState == GameState.LOBBY){
            this.players.add(player);
        } else if(gameState == GameState.RUNNING){
            this.spectators.add(player);
        }
        updatePlayerCount();
        //TODO: SEND MESSAGE
    }

    /* PLAYER-COUNT METHODS */
    private void updatePlayerCount(){
        this.playercount = players.size();
    }
}
