package com.orbitmines.discordbot;

import com.orbitmines.api.*;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableDiscord;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.discordbot.commands.*;
import com.orbitmines.discordbot.events.MessageListener;
import com.orbitmines.discordbot.handlers.DiscordSquad;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

public class DiscordBot {

    public static final String SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJlZmE4M2M5OTgyMzNlOWRlYWY3OTc1YWNlNGNkMTZiNjM2MmE4NTlkNTY4MmMzNjMxNGQxZTYwYWYifX19";

    private static DiscordBot instance;
    private Map<BotToken, JDA> jdaMap;

    private String serverId;

    private boolean isBungee;
    private Map<String, CachedPlayer> discordLinking;
    private Map<UUID, User> minecraftLinking;

    public static void main(String[] args) {
        /* Used for testing, to start all bots */
        new DiscordBot(true, BotToken.values());
    }

    public DiscordBot(boolean isBungee, BotToken... tokens) {
        instance = this;

        this.jdaMap = new HashMap<>();
        this.isBungee = isBungee;

        if (Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "DISCORD"), new Where(TableServerData.TYPE, "SERVER_ID"))) {
            serverId = Database.get().getString(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "DISCORD"), new Where(TableServerData.TYPE, "SERVER_ID"));
        } else {
            serverId = "473472016092233746";
            Database.get().insert(Table.SERVER_DATA, "DISCORD", "SERVER_ID", serverId);
        }

        for (BotToken token : tokens) {
            setupToken(token);
        }

        if (isBungee) {
            discordLinking = new HashMap<>();
            minecraftLinking = new HashMap<>();
        }

        /* Register */
        registerEvents();
        registerCommands(tokens[0]);
    }

    public static DiscordBot getInstance() {
        return instance;
    }

    public String getServerId() {
        return serverId;
    }

    public JDA getJDA(BotToken token) {
        return jdaMap.get(token);
    }

    public boolean isBungee() {
        return isBungee;
    }

    private void registerEvents() {
        for (BotToken token : jdaMap.keySet()) {
            jdaMap.get(token).addEventListener(new MessageListener(this, token));
        }
    }

    private void registerCommands(BotToken token) {
        new CommandHelp(this);

        new CommandIP(this);
        new CommandVote(this);
        new CommandTopVoters(this);
        new CommandDiscordLink(this);
        new CommandStats(this);
        new CommandSite(this);
        new CommandShop(this);
        new CommandList(this);
        new CommandListServer(this, token);
    }

    public Guild getGuild(BotToken token) {
        return jdaMap.get(token).getGuildById(serverId);
    }

    public Category getCategory(BotToken token, String name) {
        List<Category> list = getGuild(token).getCategoriesByName(name, true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public TextChannel getChannelFor(BotToken token) {
        List<TextChannel> list = getGuild(token).getTextChannelsByName(token.getChannel(), true);
        return list.size() > 0 ? list.get(0) : null;
    }
    public TextChannel getChannelFor(Guild guild, BotToken token) {
        List<TextChannel> list = guild.getTextChannelsByName(token.getChannel(), true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public TextChannel getChannel(BotToken token, ChannelType channelType) {
        List<TextChannel> list = getGuild(token).getTextChannelsByName(channelType.toString(), true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Role getRole(BotToken token, StaffRank staffRank) {
        List<Role> list = getGuild(token).getRolesByName(staffRank.toString().toLowerCase(), true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Role getRole(BotToken token, VipRank vipRank) {
        List<Role> list = getGuild(token).getRolesByName(vipRank.toString().toLowerCase(), true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Role getRole(BotToken token, CustomRole customRole) {
        List<Role> list = getGuild(token).getRolesByName(customRole.toString(), true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Emote getEmote(BotToken token, VipRank vipRank) {
        List<Emote> list = getGuild(token).getEmotesByName(vipRank.toString().toLowerCase(), true);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Emote getEmote(BotToken token, CustomEmote emote) {
        return getEmote(getGuild(token), emote);
    }

    public Emote getEmote(Guild guild, CustomEmote emote) {
        List<Emote> list = guild.getEmotesByName(emote.toString(), true);
        return list.size() > emote.index ? list.get(emote.index) : null;
    }

    private void setupToken(BotToken token) {
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(token.getToken()).setAutoReconnect(true).setGame(null).setStatus(OnlineStatus.ONLINE);

        try {
            jdaMap.put(token, jdaBuilder.buildBlocking());
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Guild guild = getGuild(token);
        if (guild.getTextChannelsByName(token.getChannel(), false).size() == 0) {
            Category category = guild.getCategoriesByName("SERVERS", true).get(0);
            category.createTextChannel(token.getChannel()).queue();
        } else {
            TextChannel channel = getChannelFor(token);
            channel.getManager().setTopic("The in-game " + token.getServer().getName() + " chat.").queue();
        }
    }

    public void setupCustomEmojis() {

    }

    public void setupCustomRoles(BotToken token) {
        Guild guild = getGuild(token);
        GuildController controller = guild.getController();
        
        for (CustomRole customRole : CustomRole.values()) {
            Role role = getRole(token, customRole);
            
            if (role == null)
                controller.createRole().setName(customRole.role).setColor(customRole.color.getAwtColor()).setMentionable(customRole.mentionable).queue();
        }
    }

    public MinecraftLinkResult discordLink(User user, String minecraftName) {
        CachedPlayer player = CachedPlayer.getPlayer(minecraftName);

        if (player == null)
            return MinecraftLinkResult.INVALID_USER;

        UUID linked = getLinkedUUID(user);
        if (linked != null && linked.toString().equals(player.getUUID().toString()))
            return MinecraftLinkResult.SAME_USER;

        if (minecraftLinking.containsKey(player.getUUID())) {
            if (minecraftLinking.get(player.getUUID()).getId().equals(user.getId())) {
                link(player.getUUID(), user);
                return MinecraftLinkResult.SETUP_COMPLETE;
            } else {
                discordLinking.put(user.getId(), player);
                return MinecraftLinkResult.WRONG_USER;
            }
        }

        discordLinking.put(user.getId(), player);

        return MinecraftLinkResult.SETTING_UP;
    }

    public MinecraftLinkResult minecraftLink(UUID uuid, String discordName, String discriminator) {
        User user = getDiscordLinking(discordName, discriminator);

        if (user == null)
            return MinecraftLinkResult.INVALID_USER;

        User linked = getLinkedUser(BotToken.DEFAULT, uuid);
        if (linked != null && linked.getId().equals(user.getId()))
            return MinecraftLinkResult.SAME_USER;

        if (discordLinking.containsKey(user.getId())) {
            if (discordLinking.get(user.getId()).getUUID().toString().equals(uuid.toString())) {
                link(uuid, user);
                return MinecraftLinkResult.SETUP_COMPLETE;
            } else {
                return MinecraftLinkResult.WRONG_USER;
            }
        }

        minecraftLinking.put(uuid, user);

        return MinecraftLinkResult.SETTING_UP;
    }

    private User getDiscordLinking(String discordName, String discriminator) {
        for (Member member : getGuild(BotToken.DEFAULT).getMembers()) {
            User user = member.getUser();

            if (user.getName().equals(discordName) && user.getDiscriminator().equals(discriminator))
                return user;
        }
        return null;
    }

    private void link(UUID uuid, User user) {
        TextChannel channel = getChannel(BotToken.DEFAULT, ChannelType.discord_link_log);
        channel.sendMessage("Linking " + user.getAsMention() + " (Id: " + user.getId() + ") to " + DiscordUtils.getDisplay(this, BotToken.DEFAULT, uuid) + "...").queue();

        discordLinking.remove(user.getId());
        minecraftLinking.remove(uuid);

        if (!Database.get().contains(Table.DISCORD, TableDiscord.UUID, new Where(TableDiscord.UUID, uuid.toString()))) {
            Database.get().insert(Table.DISCORD, uuid.toString(), user.getId());
        } else {
            /* Strip old ranks */
            Guild guild = getGuild(BotToken.DEFAULT);
            Member member = guild.getMember(getLinkedUser(BotToken.DEFAULT, uuid));
            guild.getController().removeRolesFromMember(member, member.getRoles()).queue();

            /* Create new link */
            Database.get().update(Table.DISCORD, new Set(TableDiscord.ID, user.getId()), new Where(TableDiscord.UUID, uuid.toString()));

            channel.sendMessage("Stripped roles from " + member.getUser().getAsMention() + ". (Id: " + member.getUser().getId() + ")").queue();
        }

        updateRanks(user);
    }

    public User getLinkedUser(BotToken token, UUID uuid) {
        Long id = getLinkedId(uuid);
        return id != null ? getUserById(token, id) : null;
    }

    public UUID getLinkedUUID(User user) {
        return Database.get().contains(Table.DISCORD, TableDiscord.UUID, new Where(TableDiscord.ID, user.getId())) ? UUID.fromString(Database.get().getString(Table.DISCORD, TableDiscord.UUID, new Where(TableDiscord.ID, user.getId()))) : null;
    }

    public Long getLinkedId(UUID uuid) {
        if (!Database.get().contains(Table.DISCORD, TableDiscord.ID, new Where(TableDiscord.UUID, uuid.toString())))
            return null;

        return Database.get().getLong(Table.DISCORD, TableDiscord.ID, new Where(TableDiscord.UUID, uuid.toString()));
    }

    public User getUserById(BotToken token, Long l) {
        return getJDA(token).getUserById(l);
    }

    public void updateRanks(User user) {
        CachedPlayer player = CachedPlayer.getPlayer(getLinkedUUID(user));
        Guild guild = getGuild(BotToken.DEFAULT);
        Member member = guild.getMember(user);

        List<Role> toRemove = new ArrayList<>(member.getRoles());
        List<Role> toAdd = new ArrayList<>();

        StaffRank staffRank = player.getStaffRank();
        if (staffRank != StaffRank.NONE) {
            check(getRole(BotToken.DEFAULT, staffRank), toRemove, toAdd);
            check(getRole(BotToken.DEFAULT, CustomRole.STAFF), toRemove, toAdd);
        }

        VipRank vipRank = player.getVipRank();
        if (vipRank != VipRank.NONE) {
            check(getRole(BotToken.DEFAULT, vipRank), toRemove, toAdd);
            check(getRole(BotToken.DEFAULT, CustomRole.VIP), toRemove, toAdd);
        }

        /* Update Groups */
        for (DiscordSquad group : DiscordSquad.getGroups(player.getUUID())) {
            check(group.getRole(), toRemove, toAdd);
        }

        guild.getController().modifyMemberRoles(member, toAdd, toRemove).queue();

        TextChannel channel = getChannel(BotToken.DEFAULT, ChannelType.discord_link_log);
        if (toAdd.size() > 0)
            channel.sendMessage("Added " + toString(toAdd) + " to " + user.getAsMention() + ".").queue();
        if (toRemove.size() > 0)
            channel.sendMessage("Removed " + toString(toRemove) + " from " + user.getAsMention() + ".").queue();
    }

    private String toString(List<Role> roles) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < roles.size(); i++) {
            if (i != 0)
                stringBuilder.append(", ");

            stringBuilder.append(roles.get(i).getAsMention());
        }

        return stringBuilder.toString();
    }

    private void check(Role role, List<Role> toRemove, List<Role> toAdd) {
        if (toRemove.contains(role))
            toRemove.remove(role);
        else
            toAdd.add(role);
    }

    public enum MinecraftLinkResult {

        INVALID_USER,
        SETTING_UP,
        WRONG_USER,
        SAME_USER,
        SETUP_COMPLETE;

    }

    public enum ChannelType {

        announcements(false),
        patch_notes(false),

        new_players(true),
        name_change(true),

        donations(true),
        votes(true),

        reports(true),
        punishments(true),

        command_log(true),
        sign_log(true),
        discord_link_log(false),
        private_server_log(false),
        loot_log(false),

        stats_online(false),
        stats_online_by_rank(false),
        stats_unique_players(false),
        stats_votes_total(false),
        stats_votes_monthly(false),
        stats_playtime_total(false),
//        stats_unique_joins(false),
        stats_tps_hub(false),
        stats_tps_survival(false),

        staff(false);

        private final boolean autoMute;

        ChannelType(boolean autoMute) {//TODO automute when discord adds this feature
            this.autoMute = autoMute;
        }

        public boolean autoMute() {
            return autoMute;
        }
    }

    public enum CustomEmote {

        iron_ingot,
        gold_ingot,
        diamond_item,

        orbitmines,
        kitpvp,
        prison,
        minigames,
        skyblock,
        survival,
        fog(1),
        creative,

        barrier,
        prismarine_shard,

        unknown_player;

        private final int index;

        CustomEmote() {
            this(0);
        }

        CustomEmote(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static CustomEmote from(Server server) {
            try {
                return CustomEmote.valueOf(server.toString().toLowerCase());
            } catch(NullPointerException | IllegalArgumentException ex) {
                return orbitmines;
            }
        }
    }

    public enum CustomRole {

        STAFF("staff", Color.SILVER, true),
        VIP("vip", Color.SILVER, true),

        JOIN("»", Color.LIME, true),
        LEAVE("«", Color.RED, true),

        TOP_VOTER_1("#1", Color.ORANGE, true),
        TOP_VOTER_2("#2", Color.SILVER, true),
        TOP_VOTER_3("#3", Color.MAROON, true),
        TOP_VOTER_4("#4", Color.GRAY, true),
        TOP_VOTER_5("#5", Color.GRAY, true),

        AQUA(Color.AQUA.getName(), Color.AQUA, true),
        BLACK(Color.BLACK.getName(), Color.BLACK, true),
        BLUE(Color.BLUE.getName(), Color.BLUE, true),
        FUCHSIA(Color.FUCHSIA.getName(), Color.FUCHSIA, true),
        GRAY(Color.GRAY.getName(), Color.GRAY, true),
        GREEN(Color.GREEN.getName(), Color.GREEN, true),
        LIME(Color.LIME.getName(), Color.LIME, true),
        MAROON(Color.MAROON.getName(), Color.MAROON, true),
        NAVY(Color.NAVY.getName(), Color.NAVY, true),
        ORANGE(Color.ORANGE.getName(), Color.ORANGE, true),
        PURPLE(Color.PURPLE.getName(), Color.PURPLE, true),
        RED(Color.RED.getName(), Color.RED, true),
        SILVER(Color.SILVER.getName(), Color.SILVER, true),
        TEAL(Color.TEAL.getName(), Color.TEAL, true),
        WHITE(Color.WHITE.getName(), Color.WHITE, true),
        YELLOW(Color.YELLOW.getName(), Color.YELLOW, true),
        ;

        private final String role;
        private final Color color;
        private final boolean mentionable;

        CustomRole(String role, Color color, boolean mentionable) {
            this.role = role;
            this.color = color;
            this.mentionable = mentionable;
        }

        public Color getColor() {
            return color;
        }

        public boolean isMentionable() {
            return mentionable;
        }

        @Override
        public String toString() {
            return role;
        }
    }

    public enum Images {

        /* Only PNG */

        SIGN("https://i.imgur.com/bw8AOr8.png"),
        PRISMARINE_SHARD("https://i.imgur.com/NVGeZkz.png"),

        ORBITMINES_ICON("https://i.imgur.com/E1oDT11.png"),
        KITPVP_ICON("https://i.imgur.com/P2ytvwc.png"),
        PRISON_ICON("https://i.imgur.com/OI1mz7Z.png"),
        SURVIVAL_ICON("https://i.imgur.com/lRqhPy1.png"),
        MINIGAMES_ICON("https://i.imgur.com/2xTgLAg.png"),
        FOG_ICON("https://i.imgur.com/YruBbXy.png"),
        CREATIVE_ICON("https://i.imgur.com/b1MACP9.png"),

        ORBITMINES_LOGO("https://i.imgur.com/SX8L7Qi.png"),
        KITPVP_LOGO("https://i.imgur.com/0oX5Bmd.png"),
        PRISON_LOGO("https://i.imgur.com/XSuJ1Tw.png"),
        MINIGAMES_LOGO("https://i.imgur.com/YguYCaS.png"),
        SKYBLOCK_LOGO("https://i.imgur.com/hBmypoW.png"),
        SURVIVAL_LOGO("https://i.imgur.com/3E17kYH.png"),
        FOG_LOGO("https://i.imgur.com/MG6Sw2C.png"),
        CREATIVE_LOGO("https://i.imgur.com/kyj9TSf.png")
        ;

        private final String url;

        Images(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public File getFile(String fileName) {
            if (url == null)
                return null;

            try {
                BufferedImage img = ImageIO.read(new URL(url));
                File file = new File(fileName + ".png");
                ImageIO.write(img, "png", file);

                return file;
            } catch (IOException e) {
                return null;
            }
        }

        public static Images iconFrom(Server server) {
            try {
                return Images.valueOf(server.toString().toLowerCase() + "_ICON");
            } catch(NullPointerException | IllegalArgumentException ex) {
                return ORBITMINES_ICON;
            }
        }

        public static Images logoFrom(Server server) {
            try {
                return Images.valueOf(server.toString().toLowerCase() + "_LOGO");
            } catch(NullPointerException | IllegalArgumentException ex) {
                return ORBITMINES_LOGO;
            }
        }
    }
}
