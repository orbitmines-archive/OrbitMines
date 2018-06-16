package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandWebsite extends Command {

    private String[] alias = { "/website", "/site" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return null;
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message(Message.FORMAT("Website", Color.BLUE, "")));
        cM.add(new Message("§6www.orbitmines.com"), ClickEvent.Action.OPEN_URL, new Message("https://www.orbitmines.com"), HoverEvent.Action.SHOW_TEXT, new Message("§7Visit §6www.orbitmines.com§7."));
        cM.add(new Message("§7."));

        cM.send(omp);
    }
}
