package com.orbitmines.api;

import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum ServerList {

    //TODO
    MINECRAFT_SERVERS_ORG("MinecraftServers.org", ""),
    MINECRAFT_MP_COM("Minecraft-MP.com", ""),
    MINECRAFT_SERVER_NET("Minecraft-Server.net", ""),
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

    public static ServerList fromDomain(String domainName) {
        for (ServerList serverList : ServerList.values()) {
            if (serverList.getDomainName().equalsIgnoreCase(domainName))
                return serverList;
        }

        return null;
    }
}
