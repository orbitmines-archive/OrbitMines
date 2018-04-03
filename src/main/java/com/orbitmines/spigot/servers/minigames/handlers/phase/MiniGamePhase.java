package com.orbitmines.spigot.servers.minigames.handlers.phase;

import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemInteraction;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.servers.minigames.MiniGame;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import com.orbitmines.spigot.servers.minigames.utils.GameState;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/27/2018.
 */
public abstract class MiniGamePhase {

    private final GameState state;
    private final MiniGameType miniGame;

    private int duration;

    private PreventionSet preventionSet;

    private List<Listener> events;
    private List<Command> commands;
    private List<ItemInteraction> interactions;

    public MiniGamePhase(MiniGameType miniGame, GameState state, int duration){
        this.state = state;
        this.miniGame = miniGame;
        this.preventionSet = new PreventionSet();
        this.events = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.interactions = new ArrayList<>();
        this.duration = duration;

    }

    /** ABSTRACT METHODS  */
    public abstract void start(MiniGame miniGame);

    public abstract void run(MiniGame miniGame);

    public abstract ScoreboardSet getScoreboard(MiniGamePlayer player);

    public void onEnd(MiniGame miniGame){
       if(preventionSet != null){
           preventionSet.unregister();
       }

       for(Listener listener : events){
           HandlerList.unregisterAll(listener);
       }
       events.clear();

       for(Command command : commands){
           command.unregister();
       }
       commands.clear();

       for(ItemInteraction interaction : interactions){
           interaction.unregister();
       }
       interactions.clear();
   }

    /** GETTERS */
    public MiniGameType getMiniGame() {
        return miniGame;
    }

    public GameState getState() {
        return state;
    }

    public PreventionSet getPreventionSet() {
        return preventionSet;
    }

    public MiniGameType getType(){
        return miniGame;
    }

    public List<Listener> getEvents() {
        return events;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<ItemInteraction> getInteractions() {
        return  interactions;
    }

    public int getDuration() {
        return duration;
    }

    public void prevent(World world, PreventionSet.Prevention prevent){
        if(preventionSet == null) preventionSet = new PreventionSet();
        preventionSet.prevent(world, prevent);
    }

    public void prevent(World world, PreventionSet.Prevention... prevent){
        preventionSet.prevent(world, prevent);
    }

    /** SETTERS */
    public void setDuration(int duration){
        this.duration = duration;
    }
}
