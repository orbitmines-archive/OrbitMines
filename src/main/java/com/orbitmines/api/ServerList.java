package com.orbitmines.api;

import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum ServerList {

    //TODO
    MINECRAFT_SERVERS_ORG("MinecraftServers.org", ""),
    SERVER_PACT("ServerPact.com", "https://serverpact.nl/vote-8503"),
    MINECRAFT_SERVER_NET("Minecraft-Server.net", "https://minecraft-server.net/vote/orbitmines/"),
    MINECRAFT_MP_COM("Minecraft-MP.com", ""),
    TOP_G_ORG("TopG.org", "");

    private final String domainName;
    private final String url;

    ServerList(String domainName, String url) {
        this.domainName = domainName;
        this.url = url;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getUrl() {
        return url;
    }

    public boolean canVote(Long prevVote) {
        return (System.currentTimeMillis() / 1000) - prevVote >= TimeUnit.DAYS.toSeconds(1);
    }

    public long getCooldown(Long prevVote) {
        return TimeUnit.DAYS.toSeconds(1) - ((System.currentTimeMillis() / 1000) - prevVote);
    }

    public static ServerList fromDomain(String domainName) {
        for (ServerList serverList : ServerList.values()) {
            if (serverList.getDomainName().equalsIgnoreCase(domainName))
                return serverList;
        }

        return null;
    }
}
