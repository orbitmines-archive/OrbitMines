package com.orbitmines.discordbot;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableDiscord;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.discordbot.commands.*;
import com.orbitmines.discordbot.events.MessageListener;
import com.orbitmines.discordbot.handlers.DiscordGroup;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;

import javax.security.auth.login.LoginException;
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
        registerCommands();
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

    private void registerCommands() {
        new CommandHelp(this);

        new CommandVote(this);
        new CommandDiscordLink(this);
        new CommandStats(this);
        new CommandSite(this);
        new CommandShop(this);
    }

    public Guild getGuild(BotToken token) {
        return jdaMap.get(token).getGuildById(serverId);
    }

    public TextChannel getChannelFor(BotToken token) {
        return getGuild(token).getTextChannelsByName(token.getChannel(), true).get(0);
    }

    public TextChannel getChannel(BotToken token, ChannelType channelType) {
        return getGuild(token).getTextChannelsByName(channelType.toString(), true).get(0);
    }

    public Role getRole(BotToken token, StaffRank staffRank) {
        return getGuild(token).getRolesByName(staffRank.toString().toLowerCase(), true).get(0);
    }

    public Role getRole(BotToken token, VipRank vipRank) {
        return getGuild(token).getRolesByName(vipRank.toString().toLowerCase(), true).get(0);
    }

    public Role getRole(BotToken token, CustomRole customRole) {
        return getGuild(token).getRolesByName(customRole.toString(), true).get(0);
    }

    public Emote getEmote(BotToken token, VipRank vipRank) {
        return getGuild(token).getEmotesByName(vipRank.toString().toLowerCase(), true).get(0);
    }

    public Emote getEmote(BotToken token, CustomEmote emote) {
        return getEmote(getGuild(token), emote);
    }

    public Emote getEmote(Guild guild, CustomEmote emote) {
        return guild.getEmotesByName(emote.toString(), true).get(emote.index);
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
        if (staffRank != StaffRank.NONE && staffRank != StaffRank.ADMIN) {
            check(getRole(BotToken.DEFAULT, staffRank), toRemove, toAdd);
            check(getRole(BotToken.DEFAULT, CustomRole.STAFF), toRemove, toAdd);
        }

        VipRank vipRank = player.getVipRank();
        if (vipRank != VipRank.NONE) {
            check(getRole(BotToken.DEFAULT, vipRank), toRemove, toAdd);
            check(getRole(BotToken.DEFAULT, CustomRole.VIP), toRemove, toAdd);
        }

        /* Update Groups */
        for (DiscordGroup group : DiscordGroup.getGroups(player.getUUID())) {
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
        donations(true),
        votes(true),

        reports(true),
        punishments(true),

        command_log(true),
        sign_log(true),
        discord_link_log(false),
        private_server_log(false),

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
            } catch(IllegalArgumentException ex) {
                return null;
            }
        }
    }

    public enum CustomRole {

        STAFF("staff"),
        VIP("vip"),

        JOIN("»"),
        LEAVE("«"),

        TOP_VOTER_1("#1"),
        TOP_VOTER_2("#2"),
        TOP_VOTER_3("#3");

        private final String role;

        CustomRole(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return role;
        }
    }

    public enum Images {

        SIGN("https://i.imgur.com/bw8AOr8.png"),
        PRISMARINE_SHARD("https://i.imgur.com/NVGeZkz.png"),

        ORBITMINES_ICON("https://i.imgur.com/E1oDT11.png"),

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
    }
}
