package com.orbitmines.api.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum CommandLibrary {

    /*

        Bungeecord

    */
    MSG(
            new String[] {
                    "/msg", "/message", "/m", "/tell", "/t", "/whisper", "/w"
            },

            "<player> <message>",

            ""
    ),
    DISCORDLINK(
            new String[] {
                    "/discordlink"
            },

            "<Name>#<Id>",

            ""
    ),
    REPLY(
            new String[] {
                    "/reply", "/r"
            },

            "<message>",

            ""
    ),
    HUB(
            new String[] {
                    "/hub", "/lobby"
            },

            null,

            ""
    ),
    SERVER(
            new String[] {
                    "/server"
            },

            "(server)",

            ""
    ),
    LIST(
            new String[] {
                    "/list"
            },

            null,

            ""
    ),
    WEBSITE(
            new String[] {
                    "/website", "/site"
            },

            null,

            ""
    ),
    DISCORD(
            new String[] {
                    "/discord"
            },

            null,

            ""
    ),
    SHOP(
            new String[] {
                    "/shop", "/buy", "/buycraft", "/shop", "/store", "/webshop", "/donate", "/rank"
            },

            null,

            ""
    ),
    REPORT(
            new String[] {
                    "/report"
            },

            "<player> <reason>",

            ""
    ),
    /*

        Bungeecord: MODERATOR

    */
    FIND(
            new String[] {
                    "/find"
            },

            "<player>",

            StaffRank.MODERATOR,

            ""
    ),
    SEND(
            new String[] {
                    "/send"
            },

            "<player>|<server>|all <server>",

            StaffRank.MODERATOR,

            ""
    ),
    ANNOUNCEMENT(
            new String[] {
                    "/announcement"
            },

            "list|delete|add|remove|title|subtitle",

            StaffRank.MODERATOR,

            ""
    ),
    MOTD(
            new String[] {
                    "/motd"
            },

            "view|1|2",

            StaffRank.MODERATOR,

            ""
    ),
    SILENT(
            new String[] {
                    "/silent"
            },

            null,

            StaffRank.MODERATOR,

            ""
    ),
    LOOKUP(
            new String[] {
                    "/lookup"
            },

            "<player>|<uuid>|<ip>",

            StaffRank.MODERATOR,

            ""
    ),
    MAINTENANCE(
            new String[] {
                    "/maintenance"
            },

            "<server>",

            StaffRank.MODERATOR,

            ""
    ),

    /*

        Spigot

    */
    RULES(
            null,

            new String[] {
                    "/rules"
            },

            null,

            ""
    ),
    PATCH_NOTES(
            null,

            new String[] {
                    "/patchnotes"
            },

            "(server) (version)",

            ""
    ),
    SERVERS(
            null,

            new String[] {
                    "/servers", "/serverselector"
            },

            null,

            ""
    ),
    TOPVOTERS(
            null,

            new String[] {
                    "/topvoters", "/topvoters", "/voters"
            },

            null,

            ""
    ),
    VOTE(
            null,

            new String[] {
                    "/vote"
            },

            null,

            ""
    ),
    LOOT(
            null,

            new String[] {
                    "/loot", "/spaceturtle"
            },

            null,

            ""
    ),
    FRIENDS(
            null,

            new String[] {
                    "/friends", "/friend", "/f"
            },

            null,

            ""
    ),
    DISCORDSQUAD(
            null,

            new String[] {
                    "/discordsquad", "/squad", "/discordserver"
            },

            null,

            ""
    ),
    STATS(
            null,

            new String[] {
                    "/stats", "/statistics", "/stat"
            },

            null,

            ""
    ),
    SETTINGS(
            null,

            new String[] {
                    "/settings", "/setting", "/prefs", "/preferences"
            },

            null,

            ""
    ),
    PRISMS(
            null,

            new String[] {
                    "/prisms"
            },

            null,

            ""
    ),
    SOLARS(
            null,

            new String[] {
                    "/solars"
            },

            null,

            ""
    ),
    /*

            Spigot: IRON

    */
    AFK(
            null,

            new String[] {
                    "/afk"
            },

            "(reason)",

            VipRank.IRON,

            ""
    ),
    /*

        Spigot: GOLD

    */
    NICK(
            null,

            new String[] {
                    "/nick"
            },

            "<name>|off",

            VipRank.GOLD,

            ""
    ),
    /*

        Spigot: MODERATOR

    */
    OPMODE(
            null,

            new String[] {
                    "/opmode"
            },

            null,

            StaffRank.MODERATOR,

            ""
    ),
    MOD(
            null,

            new String[] {
                    "/mod", "/ban", "/unban", "/warn", "/mute"
            },

            "<player>",

            StaffRank.MODERATOR,

            ""
    ),
    HOLOGRAM(
            null,

            new String[] {
                    "/hologram"
            },

            "list|<name> (lines|relocate|delete|create|add|remove|edit)",

            StaffRank.MODERATOR,

            ""
    ),
    INVSEE(
            null,

            new String[] {
                    "/invsee", "/inventorysee"
            },

            "<player>",

            StaffRank.MODERATOR,

            ""
    ),
    TP(
            null,

            new String[] {
                    "/tp", "/teleport"
            },

            "<player | player1> (player2 | x) (y) (z)",

            StaffRank.MODERATOR,

            ""
    ),
    FLY(
            null,

            new String[] {
                    "/fly"
            },

            "(player)",

            StaffRank.MODERATOR,

            ""
    ),
    /*

        Survival

    */
    SURVIVAL_SPAWN(
            Server.SURVIVAL,

            new String[] {
                    "/fly"
            },

            "(player)",

            ""
    ),
    SURVIVAL_REGION(
            Server.SURVIVAL,

            new String[] {
                    "/region", "/rg", "/regions"
            },

            "(number)|random",

            ""
    ),
    SURVIVAL_NEAR(
            Server.SURVIVAL,

            new String[] {
                    "/near", "/nearby", "/nearbyregion", "/nearbyrg", "/nearrg"
            },

            null,

            ""
    ),
    SURVIVAL_CLAIM(
            Server.SURVIVAL,

            new String[] {
                    "/claim", "/claimtool"
            },

            null,

            ""
    ),
    SURVIVAL_CREDITS(
            Server.SURVIVAL,

            new String[] {
                    "/credits"
            },

            null,

            ""
    ),
    SURVIVAL_PAY(
            Server.SURVIVAL,

            new String[] {
                    "/pay"
            },

            "<player> <amount>",

            ""
    ),
    SURVIVAL_PRISMSHOP(
            Server.SURVIVAL,

            new String[] {
                    "/prismshop", "/prismsshop"
            },

            null,

            ""
    ),
    SURVIVAL_HOME(
            Server.SURVIVAL,

            new String[] {
                    "/home", "/h"
            },

            "(name)",

            ""
    ),
    SURVIVAL_HOMES(
            Server.SURVIVAL,

            new String[] {
                    "/homes"
            },

            null,

            ""
    ),
    SURVIVAL_SETHOME(
            Server.SURVIVAL,

            new String[] {
                    "/sethome", "/seth"
            },

            "<name>",

            ""
    ),
    SURVIVAL_DELHOME(
            Server.SURVIVAL,

            new String[] {
                    "/delhome", "/deletehome", "/delh"
            },

            "<name>",

            ""
    ),
    SURVIVAL_WARP(
            Server.SURVIVAL,

            new String[] {
                    "/warp", "/warps"
            },

            "(name)",

            ""
    ),
    SURVIVAL_MYWARPS(
            Server.SURVIVAL,

            new String[] {
                    "/mywarps"
            },

            null,

            ""
    ),
    SURVIVAL_ACCEPT(
            Server.SURVIVAL,

            new String[] {
                    "/accept"
            },

            "<player>",

            ""
    ),
    SURVIVAL_BACK(
            Server.SURVIVAL,

            new String[] {
                    "/back"
            },

            null,

            ""
    ),
    SURVIVAL_WORKBENCH(
            Server.SURVIVAL,

            new String[] {
                    "/workbench", "/wb"
            },

            null,

            VipRank.GOLD,

            ""
    ),
    SURVIVAL_ENDERCHEST(
            Server.SURVIVAL,

            new String[] {
                    "/enderchest", "/echest", "/ec"
            },

            null,

            VipRank.DIAMOND,

            ""
    ),
    SURVIVAL_TPHERE(
            Server.SURVIVAL,

            new String[] {
                    "/tphere", "/teleporthere", "/tph"
            },

            null,

            VipRank.EMERALD,

            ""
    ),

    ;

    public static final CommandLibrary[] values = values();

    private final PluginMessage.Server serverType;
    private final Server server;

    private final String[] alias;
    private final String argsHelp;

    private final StaffRank staffRank;
    private final VipRank vipRank;

    private final String description;

    CommandLibrary(String[] alias, String argsHelp, String description) {
        this(PluginMessage.Server.BUNGEECORD, null, alias, argsHelp, null, null, description);
    }

    CommandLibrary(String[] alias, String argsHelp, VipRank vipRank, String description) {
        this(PluginMessage.Server.BUNGEECORD, null, alias, argsHelp, null, vipRank, description);
    }

    CommandLibrary(String[] alias, String argsHelp, StaffRank staffRank, String description) {
        this(PluginMessage.Server.BUNGEECORD, null, alias, argsHelp, staffRank, null, description);
    }

    CommandLibrary(Server server, String[] alias, String argsHelp, String description) {
        this(PluginMessage.Server.SPIGOT, server, alias, argsHelp, null, null, description);
    }

    CommandLibrary(Server server, String[] alias, String argsHelp, VipRank vipRank, String description) {
        this(PluginMessage.Server.SPIGOT, server, alias, argsHelp, null, vipRank, description);
    }

    CommandLibrary(Server server, String[] alias, String argsHelp, StaffRank staffRank, String description) {
        this(PluginMessage.Server.SPIGOT, server, alias, argsHelp, staffRank, null, description);
    }

    CommandLibrary(PluginMessage.Server serverType, Server server, String[] alias, String argsHelp, StaffRank staffRank, VipRank vipRank, String description) {
        this.serverType = serverType;
        this.server = server;

        this.alias = alias;
        this.argsHelp = argsHelp;

        this.staffRank = staffRank;
        this.vipRank = vipRank;

        this.description = description;
    }

    public PluginMessage.Server getServerType() {
        return serverType;
    }

    public Server getServer() {
        return server;
    }

    public String[] getAlias() {
        return alias;
    }

    public String getArgsHelp(StaffRank staffRank, VipRank vipRank) {
        return argsHelp;
    }

    public boolean isStaffCommand() {
        return getStaffRank() != null;
    }

    public StaffRank getStaffRank() {
        return staffRank;
    }

    public boolean isVipCommand() {
        return getVipRank() != null;
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public String getDescription(StaffRank staffRank, VipRank vipRank) {
        return description;
    }

    private static final Map<PluginMessage.Server, Set<CommandLibrary>> cacheLibrary = new HashMap<>();

    public static Set<CommandLibrary> getLibrary(PluginMessage.Server serverType) {
        if (cacheLibrary.containsKey(serverType))
            return cacheLibrary.get(serverType);

        Set<CommandLibrary> list = new HashSet<>();

        for (CommandLibrary commandLibrary : values) {
            if (commandLibrary.serverType == serverType)
                list.add(commandLibrary);
        }

        cacheLibrary.put(serverType, list);

        return list;
    }

    private static final Map<Server, Set<CommandLibrary>> cacheLibraryOnly = new HashMap<>();

    public static Set<CommandLibrary> getLibraryOnly(Server server) {
        if (cacheLibraryOnly.containsKey(server))
            return cacheLibraryOnly.get(server);

        Set<CommandLibrary> list = new HashSet<>();

        for (CommandLibrary commandLibrary : values) {
            if (commandLibrary.serverType == PluginMessage.Server.SPIGOT && commandLibrary.server == server)
                list.add(commandLibrary);
        }

        cacheLibraryOnly.put(server, list);

        return list;
    }

    private static final Map<Server, Set<CommandLibrary>> cacheLibraryAll = new HashMap<>();

    public static Set<CommandLibrary> getLibraryAll(Server server) {
        if (cacheLibraryAll.containsKey(server))
            return cacheLibraryAll.get(server);

        Set<CommandLibrary> list = new HashSet<>();

        for (CommandLibrary commandLibrary : values) {
            CommandLibrary unregister = null;

            /* Check if command overrides an already existing command */
            for (CommandLibrary prev : list) {
                if (!prev.alias[0].equals(commandLibrary.alias[0]))
                    continue;

                unregister = prev;
                break;
            }

            /* add to library */
            if (commandLibrary.server == null || commandLibrary.server == server)
                list.add(commandLibrary);

            /* remove duplicate command from library */
            if (unregister != null)
                list.remove(unregister);
        }

        cacheLibraryAll.put(server, list);

        return list;
    }
}
