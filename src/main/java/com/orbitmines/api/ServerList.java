package com.orbitmines.api;

import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum ServerList {

    MINECRAFT_SERVERS_ORG("MinecraftServers.org", "MinecraftServers.org", "https://minecraftservers.org/vote/508746"),
//    SERVER_PACT("ServerPact.com", "ServerPact.com", "https://serverpact.nl/vote-8503"),
    MINECRAFT_SERVERS_LIST_ORG("Minecraft-Servers-List.org", "http://www.minecraft-servers-list.org", "http://www.minecraft-servers-list.org/index.php?a=in&u=OrbitMines"),
    MINECRAFT_SERVER_NET("Minecraft-Server.net", "Minecraft-Server.net", "https://minecraft-server.net/vote/orbitmines/"),
    MINECRAFT_MP_COM("Minecraft-MP.com", "Minecraft-MP.com", "https://minecraft-mp.com/server/201145/vote/"),
    TOPMINECRAFTSERVERS_ORG("TopMinecraftServers.org", "TopMinecraftServers", "https://topminecraftservers.org/vote/4030");
//    TOP_G_ORG("TopG.org", "TopG.org", "https://topg.org/Minecraft/in-495521");

    private final String displayName;
    private final String domainName;
    private final String url;

    ServerList(String displayName, String domainName, String url) {
        this.displayName = displayName;
        this.domainName = domainName;
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
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
