package com.orbitmines.discordbot;

import com.orbitmines.discordbot.commands.CommandStats;
import com.orbitmines.discordbot.commands.TestCommand;
import com.orbitmines.discordbot.events.MessageListener;
import com.orbitmines.discordbot.utils.BotToken;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

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

//        Database database = new Database("localhost", 3306, "OrbitMines", "root", "password");
//        database.openConnection();
//        database.setupTables();

        this.jdaMap = new HashMap<>();

        for (BotToken token : tokens) {
            setupToken(token);
        }

        /* Register */
        registerEvents();
        registerCommands();

//        getGuild().getController().createEmote()
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

    public Guild getGuild() {
        return jdaMap.get(BotToken.DEFAULT).getGuildsByName("OrbitMines", true).get(0);
    }

    public TextChannel getChannelFor(BotToken token) {
        return getGuild().getTextChannelsByName(token.getChannel(), true).get(0);
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

        Guild guild = getGuild();
        if (guild.getTextChannelsByName(token.getChannel(), false).size() == 0) {
            Category category = getGuild().getCategoriesByName("SERVERS", true).get(0);
            category.createTextChannel(token.getChannel()).queue();
        } else {
            TextChannel channel = getChannelFor(token);
            channel.getManager().setTopic("The in-game " + token.getServer().getName() + " chat.").queue();
        }
    }
}
