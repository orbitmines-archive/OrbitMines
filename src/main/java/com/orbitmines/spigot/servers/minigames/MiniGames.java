package com.orbitmines.spigot.servers.minigames;

import com.google.common.io.ByteArrayDataInput;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.OrbitMinesServer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGamePlayer;
import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robin on 3/27/2018.
 */
public class MiniGames extends OrbitMinesServer {

    private HashMap<MiniGameType, List<MiniGame>> miniGames;


    public MiniGames(OrbitMines orbitMines) {
        super(orbitMines, Server.MINIGAMES, new PluginMessageHandler() {
            @Override
            public void onReceive(ByteArrayDataInput in, PluginMessage message) {

            }
        });
        this.miniGames = new HashMap<>();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new MiniGamePlayer(this, player) {
            @Override
            protected void onFirstLogin() {

            }

            @Override
            public Collection<ComponentMessage.TempTextComponent> getChatPrefix() {
                return null;
            }
        };
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        return false;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return null;
    }

    @Override
    protected void registerEvents() {

    }

    @Override
    protected void registerCommands() {

    }

    @Override
    protected void registerRunnables() {

    }

    @Override
    public void setupNpc(String npcName, Location location) {

    }

    @Override
    public GameMode getGameMode() {
        return GameMode.ADVENTURE;
    }

    @Override
    public boolean format(CachedPlayer sender, OMPlayer receiver, Color color, String string, List<ComponentMessage.TempTextComponent> list) {
        return false;
    }

    public List<MiniGame> getMiniGames(MiniGameType type){
        return miniGames.get(type);
    }
}
