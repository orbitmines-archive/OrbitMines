package com.orbitmines.api;

import java.util.HashSet;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum PluginMessage {

    CONNECT(Server.BUNGEECORD),
    KICK(Server.BUNGEECORD),

    LOGIN_2FA(Server.BUNGEECORD),

    ;

    public static final String CHANNEL = "OrbitMines";
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
