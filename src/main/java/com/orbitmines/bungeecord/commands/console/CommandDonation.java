package com.orbitmines.bungeecord.commands.console;

import com.orbitmines.bungeecord.handlers.cmd.ConsoleCommand;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandDonation extends ConsoleCommand {

    private String[] alias = { "/donation" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(ChatEvent event, String[] a) {

    }
}
