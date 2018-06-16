package com.orbitmines.discordbot.events;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.discordbot.handlers.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();

        String[] a = message.getContentRaw().split(" ");
        Command command = Command.getCommand(a[0]);

        if (command == null)
            return;

        command.dispatch(event, user, channel, message, a);
    }
}
