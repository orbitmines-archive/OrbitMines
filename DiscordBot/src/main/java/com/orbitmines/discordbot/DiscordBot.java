package com.orbitmines.discordbot;

import com.orbitmines.api.database.Database;
import com.orbitmines.discordbot.commands.CommandStats;
import com.orbitmines.discordbot.commands.TestCommand;
import com.orbitmines.discordbot.events.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;

import javax.security.auth.login.LoginException;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

public class DiscordBot {

    private static DiscordBot instance;
    private JDA jda;

    public static void main(String[] args) {
        new DiscordBot(args);
    }

    public DiscordBot(String[] args) {
        instance = this;

        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken("NDU3NjIzNzI4MDMzODkwMzA0.Dgb74A.phP4ztzrfLPQ-j55f996t4BPZy0").setAutoReconnect(true).setStatus(OnlineStatus.ONLINE);

        try {
            jda = jdaBuilder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Database database = new Database("localhost", 3306, "OrbitMines", "root", "password");
        database.openConnection();
        database.setupTables();

        /* Register */
        registerEvents();
        registerCommands();
    }

    public static DiscordBot getInstance() {
        return instance;
    }

    public JDA getJDA() {
        return jda;
    }

    private void registerEvents() {
        jda.addEventListener(new MessageListener());
    }

    private void registerCommands() {
        new TestCommand();
        new CommandStats(this);
    }

    public Guild getGuild() {
        return jda.getGuildsByName("OrbitMines", true).get(0);
    }
}
