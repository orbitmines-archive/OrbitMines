package com.orbitmines.api.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;

import java.util.*;

public enum CommandLibrary {

    /*

        Bungeecord

    */
    HELP(
            new String[] {
                    "/help"
            },

            null,

            "Show list of commands."
    ),
    MSG(
            new String[] {
                    "/msg", "/message", "/m", "/tell", "/t", "/whisper", "/w"
            },

            "<player> <message>",

            "Message a player."
    ),
    DISCORDLINK(
            new String[] {
                    "/discordlink"
            },

            "<Name>#<Id>",

            "Link your Discord to OrbitMines."
    ),
    REPLY(
            new String[] {
                    "/reply", "/r"
            },

            "<message>",

            "Reply to a private message."
    ),
    HUB(
            new String[] {
                    "/hub", "/lobby"
            },

            null,

            "Go to the Hub."
    ),
    SERVER(
            new String[] {
                    "/server"
            },

            "(server)",

            "Go to a server."
    ),
    LIST(
            new String[] {
                    "/list"
            },

            null,

            "Open list of online players."
    ),
    WEBSITE(
            new String[] {
                    "/website", "/site"
            },

            null,

            "Get the link to the website."
    ),
    DISCORD(
            new String[] {
                    "/discord"
            },

            null,

            "Get the link to the Discord server."
    ),
    SHOP(
            new String[] {
                    "/shop", "/buy", "/buycraft", "/shop", "/store", "/webshop", "/donate", "/rank"
            },

            null,

            "Get the link to the OrbitMines Buycraft store."
    ),
    REPORT(
            new String[] {
                    "/report"
            },

            "<player> <reason>",

            "Report a player."
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

            "Show what server a player is on."
    ),
    SEND(
            new String[] {
                    "/send"
            },

            "<player>|<server>|all <server>",

            StaffRank.MODERATOR,

            "Send a player to a server."
    ),
    ANNOUNCEMENT(
            new String[] {
                    "/announcement"
            },

            "list|delete|add|remove|title|subtitle",

            StaffRank.MODERATOR,

            "Edit server announcements."
    ),
    MOTD(
            new String[] {
                    "/motd"
            },

            "view|1|2",

            StaffRank.MODERATOR,

            "Edit message of the day."
    ),
    SILENT(
            new String[] {
                    "/silent"
            },

            null,

            StaffRank.MODERATOR,

            "Become invisible."
    ),
    LOOKUP(
            new String[] {
                    "/lookup"
            },

            "<player>|<uuid>|<ip>",

            StaffRank.MODERATOR,

            "Show player information."
    ),
    MAINTENANCE(
            new String[] {
                    "/maintenance"
            },

            "<server>",

            StaffRank.MODERATOR,

            "Set a server in maintenance-mode."
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

            "Open the rules."
    ),
    PATCH_NOTES(
            null,

            new String[] {
                    "/patchnotes"
            },

            "(server) (version)",

            "Open patch notes."
    ),
    SERVERS(
            null,

            new String[] {
                    "/servers", "/serverselector"
            },

            null,

            "Open server selector."
    ),
    TOPVOTERS(
            null,

            new String[] {
                    "/topvoters", "/topvoters", "/voters"
            },

            null,

            "Show the topvoters of the month."
    ),
    VOTE(
            null,

            new String[] {
                    "/vote"
            },

            null,

            "Get the voting links."
    ),
    LOOT(
            null,

            new String[] {
                    "/loot", "/spaceturtle"
            },

            null,

            "Open loot inventory."
    ),
    FRIENDS(
            null,

            new String[] {
                    "/friends", "/friend", "/f"
            },

            null,

            "Open friends inventory."
    ),
    DISCORDSQUAD(
            null,

            new String[] {
                    "/discordsquad", "/squad", "/discordserver"
            },

            null,

            "Open DiscordSquad inventory."
    ),
    STATS(
            null,

            new String[] {
                    "/stats", "/statistics", "/stat"
            },

            null,

            "Open stats inventory."
    ),
    SETTINGS(
            null,

            new String[] {
                    "/settings", "/setting", "/prefs", "/preferences"
            },

            null,

            "Open settings inventory."
    ),
    PRISMS(
            null,

            new String[] {
                    "/prisms"
            },

            null,

            "Show prism count."
    ),
    SOLARS(
            null,

            new String[] {
                    "/solars"
            },

            null,

            "Show solar count."
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

            "Set yourself afk."
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

            "Set a nickname for yourself."
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

            "Go into opmode."
    ),
    MOD(
            null,

            new String[] {
                    "/mod", "/ban", "/unban", "/warn", "/mute"
            },

            "<player>",

            StaffRank.MODERATOR,

            "Open the moderator inventory."
    ),
    HOLOGRAM(
            null,

            new String[] {
                    "/hologram"
            },

            "list|<name> (lines|relocate|delete|create|add|remove|edit)",

            StaffRank.MODERATOR,

            "Set a hologram."
    ),
    INVSEE(
            null,

            new String[] {
                    "/invsee", "/inventorysee"
            },

            "<player>",

            StaffRank.MODERATOR,

            "See inventory of a player."
    ),
    TP(
            null,

            new String[] {
                    "/tp", "/teleport"
            },

            "<player | player1> (player2 | x) (y) (z)",

            StaffRank.MODERATOR,

            "Teleport to a player."
    ),
    FLY(
            null,

            new String[] {
                    "/fly"
            },

            "(player)",

            StaffRank.MODERATOR,

            "Go into fly mode."
    ),
    /*

        Survival

    */
    SURVIVAL_SPAWN(
            Server.SURVIVAL,

            new String[] {
                    "/spawn"
            },

            "(player)",

            "Teleport to spawn."
    ),
    SURVIVAL_REGION(
            Server.SURVIVAL,

            new String[] {
                    "/region", "/rg", "/regions"
            },

            "(number)|random",

            "Open region inventory."
    ),
    SURVIVAL_NEAR(
            Server.SURVIVAL,

            new String[] {
                    "/near", "/nearby", "/nearbyregion", "/nearbyrg", "/nearrg"
            },

            null,

            "Show nearby region."
    ),
    SURVIVAL_CLAIM(
            Server.SURVIVAL,

            new String[] {
                    "/claim", "/claimtool"
            },

            null,

            "Receive claiming tool."
    ),
    SURVIVAL_CLAIMS(
            Server.SURVIVAL,

            new String[] {
                    "/claims", "/claimlist"
            },

            null,

            "Open claim inventory."
    ),
    SURVIVAL_PET(
            Server.SURVIVAL,

            new String[] {
                    "/pet", "/petticket"
            },

            null,

            "Get pet-ownershipticket."
    ),
    SURVIVAL_CREDITS(
            Server.SURVIVAL,

            new String[] {
                    "/credits"
            },

            null,

            "Show credit count."
    ),
    SURVIVAL_PAY(
            Server.SURVIVAL,

            new String[] {
                    "/pay"
            },

            "<player> <amount>",

            "Pay credits to a player."
    ),
    SURVIVAL_PRISMSHOP(
            Server.SURVIVAL,

            new String[] {
                    "/prismshop", "/prismsshop"
            },

            null,

            "Open prismshop."
    ),
    SURVIVAL_HOME(
            Server.SURVIVAL,

            new String[] {
                    "/home", "/h"
            },

            "(name)",

            "Teleport to a set home."
    ),
    SURVIVAL_HOMES(
            Server.SURVIVAL,

            new String[] {
                    "/homes"
            },

            null,

            "Show list of homes."
    ),
    SURVIVAL_SETHOME(
            Server.SURVIVAL,

            new String[] {
                    "/sethome", "/seth"
            },

            "<name>",

            "Set a home."
    ),
    SURVIVAL_DELHOME(
            Server.SURVIVAL,

            new String[] {
                    "/delhome", "/deletehome", "/delh"
            },

            "<name>",

            "Delete a home."
    ),
    SURVIVAL_WARP(
            Server.SURVIVAL,

            new String[] {
                    "/warp", "/warps"
            },

            "(name)",

            "Teleport to a set warp."
    ),
    SURVIVAL_MYWARPS(
            Server.SURVIVAL,

            new String[] {
                    "/mywarps"
            },

            null,

            "Open your warp inventory."
    ),
    SURVIVAL_ACCEPT(
            Server.SURVIVAL,

            new String[] {
                    "/accept"
            },

            "<player>",

            "Accept a teleport request."
    ),
    SURVIVAL_BACK(
            Server.SURVIVAL,

            new String[] {
                    "/back"
            },

            null,

            "Teleport back to previous location."
    ),
    SURVIVAL_WORKBENCH(
            Server.SURVIVAL,

            new String[] {
                    "/workbench", "/wb"
            },

            null,

            VipRank.GOLD,

            "Open crafting table."
    ),
    SURVIVAL_ENDERCHEST(
            Server.SURVIVAL,

            new String[] {
                    "/enderchest", "/echest", "/ec"
            },

            null,

            VipRank.DIAMOND,

            "Open enderchest."
    ),
    SURVIVAL_FLY(
            Server.SURVIVAL,

            new String[] {
                    "/fly"
            },

            null,

            VipRank.DIAMOND,

            "Go into fly mode."
    ),
    SURVIVAL_TP(
            Server.SURVIVAL,

            new String[] {
                    "/tp", "/teleport"
            },

            "<player>",

            VipRank.GOLD,

            "Send a tp request to a player."
    ),
    SURVIVAL_TPHERE(
            Server.SURVIVAL,

            new String[] {
                    "/tphere", "/teleporthere", "/tph"
            },

            "<player>",

            VipRank.EMERALD,

            "Send a tphere request to a player."
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

    private static final Map<PluginMessage.Server, List<CommandLibrary>> cacheLibrary = new HashMap<>();

    public static List<CommandLibrary> getLibrary(PluginMessage.Server serverType) {
        if (cacheLibrary.containsKey(serverType))
            return cacheLibrary.get(serverType);

        List<CommandLibrary> list = new ArrayList<>();

        for (CommandLibrary commandLibrary : values) {
            if (commandLibrary.serverType == serverType)
                list.add(commandLibrary);
        }

        cacheLibrary.put(serverType, list);

        return list;
    }

    private static final Map<Server, List<CommandLibrary>> cacheLibraryOnly = new HashMap<>();

    public static List<CommandLibrary> getLibraryOnly(Server server) {
        if (cacheLibraryOnly.containsKey(server))
            return cacheLibraryOnly.get(server);

        List<CommandLibrary> list = new ArrayList<>();

        for (CommandLibrary commandLibrary : values) {
            if (commandLibrary.serverType == PluginMessage.Server.SPIGOT && commandLibrary.server == server)
                list.add(commandLibrary);
        }

        cacheLibraryOnly.put(server, list);

        return list;
    }

    private static final Map<Server, List<CommandLibrary>> cacheLibraryAll = new HashMap<>();

    public static List<CommandLibrary> getLibraryAll(Server server) {
        if (cacheLibraryAll.containsKey(server))
            return cacheLibraryAll.get(server);

        List<CommandLibrary> list = new ArrayList<>();

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
