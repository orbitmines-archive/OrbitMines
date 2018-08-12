package com.orbitmines.api;

import java.util.HashSet;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum PluginMessage {

    CONNECT(Server.BUNGEECORD),
    KICK(Server.BUNGEECORD),

    SET_LOGGING_IN(Server.BUNGEECORD),
    LOGIN_2FA(Server.BUNGEECORD),

    PUNISH(Server.BUNGEECORD),
    PARDON(Server.BUNGEECORD),
    MUTE(Server.BUNGEECORD),

    SHUTDOWN(Server.SPIGOT),

    MESSAGE_PLAYER(Server.BUNGEECORD),

    CHECK_VOTE_CACHE(Server.BUNGEECORD),
    UPDATE_RANKS(Server.BUNGEECORD),
    UPDATE_LANGUAGE(Server.BUNGEECORD),
    UPDATE_SILENT(Server.BUNGEECORD),
    CHECK_DISCORD_LINK_ACHIEVEMENT(Server.SPIGOT),

    FAVORITE_FRIEND_MESSAGE(Server.BUNGEECORD),
    UPDATE_FRIENDS(Server.SPIGOT),

    DISCORD_GROUP_ACTION(Server.BUNGEECORD),
    UPDATE_DISCORD_GROUP_DATA(Server.SPIGOT),

    UPDATE_SETTINGS(Server.BUNGEECORD),
    SERVER_SWITCH(Server.BUNGEECORD);

    public static final String CHANNEL = "orbitmines:main";
    public static final String CHANNEL_BUNGEECORD = "BungeeCord";

    private final Server target;

    PluginMessage(Server target) {
        this.target = target;
    }

    public Server getTarget() {
        return target;
    }

    public static Set<PluginMessage> values(Server target) {
        Set<PluginMessage> values = new HashSet<>();
        for (PluginMessage msg : values()) {
            if (msg.target == target)
                values.add(msg);
        }
        return values;
    }

    public enum Server {

        SPIGOT,
        BUNGEECORD;

    }
}
