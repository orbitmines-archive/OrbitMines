package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.chat.ComponentMessage;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.event.ChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandShop extends Command {

    public CommandShop() {
        super(CommandLibrary.SHOP);
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        ComponentMessage cM = new ComponentMessage();
        cM.add(new Message(Message.FORMAT("Shop", Color.BLUE, "")));
        cM.add(new Message("§3orbitmines.buycraft.net"), ClickEvent.Action.OPEN_URL, new Message("https://orbitmines.buycraft.net"), HoverEvent.Action.SHOW_TEXT, new Message("§7" + omp.lang("Open", "Visit") + " §3orbitmines.buycraft.net§7."));
        cM.add(new Message("§7."));

        cM.send(omp);
    }
}
