package com.orbitmines.spigot;

import com.orbitmines.api.*;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordSpigotUtils;
import com.orbitmines.discordbot.utils.DiscordUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.PluginMessageHandler;
import com.orbitmines.spigot.api.handlers.PreventionSet;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.options.ApiOption;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.hub.Hub;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.survival.Survival;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class OrbitMinesServer {

    protected final OrbitMines orbitMines;
    protected final Server server;
    private final PluginMessageHandler messageHandler;

    protected DiscordBot discord;
    protected BotToken token;

    protected final PreventionSet preventionSet;
    private BossBar maintenanceBossBar;

    public OrbitMinesServer(OrbitMines orbitMines, Server server, PluginMessageHandler messageHandler) {
        this.orbitMines = orbitMines;
        this.server = server;
        this.messageHandler = messageHandler;

        this.preventionSet = new PreventionSet();
        this.maintenanceBossBar = Bukkit.createBossBar(server.getDisplayName() + " §7§l| " + Server.Status.MAINTENANCE.getDisplayName(), BarColor.PINK, BarStyle.SOLID);

        /* Setup Discord */
        try {
            token = BotToken.from(server);
            discord = new DiscordBot(false, token);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        registerEvents();

        /* register commands */
        registerCommands();

        registerRunnables();

        new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 2) {
            @Override
            public void run() {
                Server.Status status = server.getStatus();

                if (status == Server.Status.RESTARTING) {
                    Bukkit.shutdown();
                } else if (status != Server.Status.MAINTENANCE) {
                    if (status != Server.Status.ONLINE)
                        server.setStatus(Server.Status.ONLINE);
                    else
                        server.resetLastUpdate();

                    maintenanceBossBar.removeAll();
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!maintenanceBossBar.getPlayers().contains(player))
                            maintenanceBossBar.addPlayer(player);
                    }
                }
            }
        };

        new SpigotRunnable(SpigotRunnable.TimeUnit.MINUTE, 5) {
            @Override
            public void run() {
                MinecraftServer server = MinecraftServer.getServer();
                Database.get().insert(Table.STATS_TPS, System.currentTimeMillis() + "", OrbitMinesServer.this.server.toString(), ((long) ((server.recentTps[0] + server.recentTps[1] + server.recentTps[2]) / 3) * 10000L) + "");
            }
        };
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract OMPlayer newPlayerInstance(Player player);

    /* OMPlayer is not yet initiated here */
    public abstract boolean teleportToSpawn(Player player);

    /* OMPlayer is not yet initiated here */
    public abstract Location getSpawnLocation(Player player);

    public abstract GameMode getGameMode();

    /* Word Formatting; [item], /cmd etc.. -> Add component to list */
    public abstract boolean format(CachedPlayer sender, OMPlayer receiver, Color color, String string, List<ComponentMessage.TempTextComponent> list);

    protected abstract void registerEvents();

    protected abstract void registerCommands();

    protected abstract void registerRunnables();

    public abstract void setupNpc(String npcName, Location location);

    protected void setup(ApiOption... options) {
        for (ApiOption option : options) {
            option.setup(orbitMines);
        }
    }

    public void fromDiscord(Member member, net.dv8tion.jda.core.entities.Message message) {
        StaffRank staffRank = StaffRank.NONE;
        VipRank vipRank = VipRank.NONE;

        for (Role role : member.getRoles()) {
            try {
                staffRank = StaffRank.valueOf(role.getName().toUpperCase());
            } catch (IllegalArgumentException ex) {
                try {
                    vipRank = VipRank.valueOf(role.getName().toUpperCase());
                } catch (IllegalArgumentException ex1) { }
            }
        }

        ComponentMessage cM = new ComponentMessage();

        String rankPrefix = staffRank != StaffRank.NONE ? staffRank.getPrefix(staffRank.getPrefixColor()) : vipRank.getPrefix(vipRank.getPrefixColor());
        Color chatColor = staffRank != StaffRank.NONE ? staffRank.getChatColor() : vipRank.getChatColor();
        Color prefixColor = staffRank != StaffRank.NONE ? staffRank.getPrefixColor() : vipRank.getPrefixColor();

        cM.add(new Message("§9§lDISCORD§r"), ClickEvent.Action.OPEN_URL, new Message("https://discord.gg/QjVGJMe"), HoverEvent.Action.SHOW_TEXT, new Message("§7Open §9https://discord.gg/QjVGJMe"));
        cM.add(new Message(" §7» "));

        String name = rankPrefix + "@" + member.getEffectiveName();

        UUID uuid = discord.getLinkedUUID(member.getUser());
        CachedPlayer sender = null;
        String playerName = null;

        if (uuid != null) {
            sender = CachedPlayer.getPlayer(uuid);
            playerName = sender.getRankPrefixColor().getChatColor() + sender.getPlayerName();
        }

        cM.add(new Message(name), HoverEvent.Action.SHOW_TEXT, new Message(prefixColor.getChatColor() + "@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n§7Rank: " + (staffRank == StaffRank.NONE ? vipRank.getDisplayName() : (vipRank == VipRank.NONE ? staffRank.getDisplayName() : (staffRank.getDisplayName() + " §7/ " + vipRank.getDisplayName()))) + "\n§7IGN: " + (playerName == null ? StaffRank.NONE.getDisplayName() : playerName)));

        cM.add(new Message(" §7» "));

        /* Filter discord mentions */
        String filtered = DiscordUtils.filterFromDiscord(chatColor.getChatColor(), message);

        /* File Attachment */
        ComponentMessage.TempTextComponent attachmentComponent = null;
        if (message.getAttachments().size() != 0) {
            net.dv8tion.jda.core.entities.Message.Attachment attachment = message.getAttachments().get(0);
            attachmentComponent = new ComponentMessage.TempTextComponent(new Message((filtered.equals("") ? "" : " ") + "(File: §l" + attachment.getFileName() + "§r§3)"), ClickEvent.Action.OPEN_URL, new Message(attachment.getUrl()), HoverEvent.Action.SHOW_TEXT, new Message("§7Klik hier om §3§l" + attachment.getFileName() + "§7 te openen.", "§7Click here to open §3§l" + attachment.getFileName() + "§7.")).setChatColor(ChatColor.DARK_AQUA);
        }

        /* Send per player for commands&mentions */
        for (OMPlayer player : OMPlayer.getPlayers()) {
            ComponentMessage componentMessage = new ComponentMessage(cM);

            for (ComponentMessage.TempTextComponent component : DiscordSpigotUtils.formatMessage(this, sender, player, chatColor, filtered)) {
                componentMessage.add(component);
            }

            if (attachmentComponent != null)
                componentMessage.add(attachmentComponent);

            componentMessage.send(player);
        }
    }

    public void toMinecraft(OMPlayer omp, String message) {
        CachedPlayer sender = CachedPlayer.getPlayer(omp.getUUID());

        ComponentMessage cM = new ComponentMessage();

        for (ComponentMessage.TempTextComponent component : omp.getChatPrefix()) {
            cM.add(component);
        }

        cM.add(DiscordSpigotUtils.getPlayerMention(omp, omp.getRankPrefix() + omp.getName()));

        cM.add("§7 » ");

        Color rankChatColor = omp.getRankChatColor();

        for (OMPlayer player : OMPlayer.getPlayers()) {
            ComponentMessage componentMessage = new ComponentMessage(cM);

            for (ComponentMessage.TempTextComponent component : DiscordSpigotUtils.formatMessage(this, sender, player, rankChatColor, message)) {
                componentMessage.add(component);
            }

            componentMessage.send(player);
        }
    }

    public void toDiscord(AsyncPlayerChatEvent event, OMPlayer omp) {
        getDiscordChannel().sendMessage(DiscordSpigotUtils.getDisplay(discord, token, omp) + " » " + DiscordUtils.filterToDiscord(discord, token, event.getMessage())).queue();
    }

    protected void registerEvents(Listener... listeners) {
        PluginManager pluginManager = orbitMines.getServer().getPluginManager();
        for (Listener l : listeners) {
            pluginManager.registerEvents(l, orbitMines);
        }
    }

    public static OrbitMinesServer getServer(OrbitMines orbitMines, Server server) {
        switch (server) {

            case KITPVP:
                return new KitPvP(orbitMines);
            case PRISON:
                return null;
            case CREATIVE:
                return null;
            case HUB:
                return new Hub(orbitMines);
            case SURVIVAL:
                return new Survival(orbitMines);
            case SKYBLOCK:
                return null;
            case FOG:
                return null;
            case MINIGAMES:
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    public OrbitMines getOrbitMines() {
        return orbitMines;
    }

    public Server getServer() {
        return server;
    }

    public PluginMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public DiscordBot getDiscord() {
        return discord;
    }

    public TextChannel getDiscordChannel() {
        return discord.getChannelFor(token);
    }

    public BotToken getToken() {
        return token;
    }

    public PreventionSet getPreventionSet() {
        return preventionSet;
    }
}
