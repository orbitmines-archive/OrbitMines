package com.orbitmines.spigot.servers.minigames.handlers.team;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.team.place.Place;
import com.orbitmines.spigot.servers.minigames.utils.GameState;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robin on 3/27/2018.
 */
public class Team {

    private Settings settings;

    private Set<MiniGamePlayer> teamMates;
    private Set<MiniGamePlayer> deadTeamMates;

    public Team(Settings settings){
        this.settings = settings;
        this.teamMates = new HashSet<>();
        this.deadTeamMates = new HashSet<>();
    }

    /** TEAMMATES METHODS */
    public void add(MiniGamePlayer player){
        this.teamMates.add(player);
    }

    public Set<MiniGamePlayer> getTeamMates() {
        return teamMates;
    }

    public boolean contains(MiniGamePlayer player){
        return teamMates.contains(player);
    }

    /** BOOLEANS */
    public boolean containsAlive(MiniGamePlayer player){
        return contains(player) && !deadTeamMates.contains(player);
    }

    public boolean isFull(){
        return settings.getMaxPlayers() == teamMates.size();
    }

    public boolean isDead(){
        return teamMates.containsAll(deadTeamMates);
    }

    public boolean isDead(MiniGamePlayer player){
        return deadTeamMates.contains(player);
    }

    /** TEAM METHODS */
    public void teleport(Location location){

    }

    public void gainReward(Place place){
        for (MiniGamePlayer player : teamMates) {
            if(place.getPlace() == Place.FIRST){
                player.getData(player.getGame().getType()).addWin();
            }
            place.gainReward(player);
        }
    }

    public void die(MiniGamePlayer player){
        deadTeamMates.add(player);
    }

    public void playSound(Sound sound, float f1, float f2) {
        for (MiniGamePlayer mbp : teamMates) {
            mbp.getPlayer().playSound(mbp.getLocation(), sound, f1, f2);
        }
    }

    public static void generateTeams(MiniGame miniGame){
        if(miniGame.getState() == GameState.LOBBY){
            Team team = new Team(miniGame.getType().getSettings());
            Set<MiniGamePlayer> players = miniGame.getPlayers();
            while(players.size() != 0){
                if(team.isFull()){
                    miniGame.getTeams().add(team);
                    team = new Team(miniGame.getType().getSettings());
                }
                MiniGamePlayer p = (MiniGamePlayer) players.toArray()[RandomUtils.i(players.size())];
                team.add(p);
                players.remove(p);
            }
            if(team.getTeamMates().size() > 0){
                miniGame.getTeams().add(team);
            }
        }
    }

    /** SETTINGS CLASS */
    public static class Settings {

        private int maxPlayers;

        private boolean friendlyFire;
        private boolean seeFriendlySpectator;
        private boolean seeFriendlyMessages;

        public Settings(int maxPlayers, boolean friendlyFire, boolean seeFriendlySpectator, boolean seeFriendlyMessages) {
            this.maxPlayers = maxPlayers;
            this.friendlyFire = friendlyFire;
            this.seeFriendlySpectator = seeFriendlySpectator;
            this.seeFriendlyMessages = seeFriendlyMessages;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }

        public boolean isFriendlyFire() {
            return friendlyFire;
        }

        public boolean isSeeFriendlySpectator() {
            return seeFriendlySpectator;
        }

        public boolean isSeeFriendlyMessages() {
            return seeFriendlyMessages;
        }
    }
}
