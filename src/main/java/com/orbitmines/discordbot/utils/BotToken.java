package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Server;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;

public enum BotToken {

    DEFAULT("NDU3NjIzNzI4MDMzODkwMzA0.Dgb74A.phP4ztzrfLPQ-j55f996t4BPZy0", Server.HUB, "hub_chat", "orbitmines"),
    SURVIVAL("NDU4MDA4MDc3OTMzODcxMTE1.DghYyA.rDC9poB4wz0CxScI7dvXQi6tYJk", Server.SURVIVAL, "survival_chat", "survival"),
    UHSURVIVAL("NDYxNDgwNzYwODU2MjgxMTM4.DhT69w.r8LAkXVzaXYEV8F9ZC9xT7DH0S8", Server.UHSURVIVAL, "uhsurvival_chat", "barrier");

    private final String token;
    private final Server server;
    private final String channel;
    private final String emote;

    BotToken(String token, Server server, String channel, String emote) {
        this.token = token;
        this.server = server;
        this.channel = channel;
        this.emote = emote;
    }

    public String getToken() {
        return token;
    }

    public Server getServer() {
        return server;
    }

    public Emote getServerEmote(Guild guild) {
        return guild.getEmotesByName(emote, true).get(0);
    }

    public String getChannel() {
        return channel;
    }

    public static BotToken from(Server server) {
        for (BotToken token : values()) {
            if (token.getServer() == server)
                return token;
        }

        return BotToken.DEFAULT;
    }
}
