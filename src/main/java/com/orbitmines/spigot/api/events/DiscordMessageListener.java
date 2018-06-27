package com.orbitmines.spigot.api.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.OrbitMines;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordMessageListener extends ListenerAdapter {

    private final OrbitMines orbitMines;

    public DiscordMessageListener(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member member = event.getMember();

        if (isBot(member))
            return;

        MessageChannel channel = event.getChannel();

        if (!channel.getName().equals(orbitMines.getServerHandler().getDiscordChannel().getName()))
            return;

        Message message = event.getMessage();

        orbitMines.getServerHandler().fromDiscord(member, message);
    }

    private boolean isBot(Member member) {
        for (Role role : member.getRoles()) {
            if (role.getName().equalsIgnoreCase("bots"))
                return true;
        }
        return false;
    }
}
