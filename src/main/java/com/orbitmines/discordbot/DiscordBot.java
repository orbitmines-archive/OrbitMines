package com.orbitmines.discordbot;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.discordbot.commands.CommandStats;
import com.orbitmines.discordbot.commands.TestCommand;
import com.orbitmines.discordbot.events.MessageListener;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

public class DiscordBot {

    private static DiscordBot instance;
    private Map<BotToken, JDA> jdaMap;

    public static void main(String[] args) {
        /* Used for testing, to start all bots */
        new DiscordBot(BotToken.values());
    }

    public DiscordBot(BotToken... tokens) {
        instance = this;

        this.jdaMap = new HashMap<>();

        for (BotToken token : tokens) {
            setupToken(token);
        }

        /* Register */
        registerEvents();
        registerCommands();
    }

    public static DiscordBot getInstance() {
        return instance;
    }

    public JDA getJDA(BotToken token) {
        return jdaMap.get(token);
    }

    private void registerEvents() {
        for (BotToken token : jdaMap.keySet()) {
            jdaMap.get(token).addEventListener(new MessageListener(token));
        }
    }

    private void registerCommands() {
        new TestCommand();
        new CommandStats(this);
    }

    public Guild getGuild(BotToken token) {
        return jdaMap.get(token).getGuildsByName("OrbitMines", true).get(0);
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

    public Emote getEmote(BotToken token, VipRank vipRank) {
        return getGuild(token).getEmotesByName(vipRank.toString().toLowerCase(), true).get(0);
    }

    public Emote getEmote(BotToken token, CustomEmote emote) {
        return getGuild(token).getEmotesByName(emote.toString(), true).get(emote.index);
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

    public enum ChannelType {

        patch_notes(false),

        new_players(true),
        donations(true),
        votes(true),

        reports(true),
        punishments(true),

        command_log(true),
        sign_log(true),

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
        prismarine_shard;

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
    }

    public enum Images {

        SIGN("https://i.imgur.com/bw8AOr8.png");

        private final String url;

        Images(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
