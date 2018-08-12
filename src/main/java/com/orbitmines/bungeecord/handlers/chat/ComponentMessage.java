package com.orbitmines.bungeecord.handlers.chat;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ComponentMessage {

    private List<TempTextComponent> tcs;

    public ComponentMessage() {
        tcs = new ArrayList<>();
    }

    public void add(String component) {
        add(new Message(component), null, null, null, null);
    }

    public void add(Message component) {
        add(component, null, null, null, null);
    }

    public void add(String component, ClickEvent.Action clickAction, String clickEvent) {
        add(new Message(component), clickAction, new Message(clickEvent), null, null);
    }

    public void add(Message component, ClickEvent.Action clickAction, Message clickEvent) {
        add(component, clickAction, clickEvent, null, null);
    }

    public void add(String component, HoverEvent.Action hoverAction, String hoverEvent) {
        add(new Message(component), null, null, hoverAction, new Message(hoverEvent));
    }

    public void add(Message component, HoverEvent.Action hoverAction, Message hoverEvent) {
        add(component, null, null, hoverAction, hoverEvent);
    }

    public void add(String component, ClickEvent.Action clickAction, String clickEvent, HoverEvent.Action hoverAction, String hoverEvent) {
        add(new Message(component), clickAction, new Message(clickEvent), hoverAction, new Message(hoverEvent));
    }

    public void add(Message component, ClickEvent.Action clickAction, Message clickEvent, HoverEvent.Action hoverAction, Message hoverEvent) {
        tcs.add(new TempTextComponent(component, clickAction, clickEvent, hoverAction, hoverEvent));
    }

    public void send(BungeePlayer player) {
        send(Collections.singletonList(player));
    }

    public void send(BungeePlayer... players) {
        send(Arrays.asList(players));
    }

    public void send(Collection<? extends BungeePlayer> players) {
        Map<Language, List<BungeePlayer>> perLanguage = new HashMap<>();
        for (BungeePlayer player : players) {
            if (!perLanguage.containsKey(player.getLanguage()))
                perLanguage.put(player.getLanguage(), new ArrayList<>());

            perLanguage.get(player.getLanguage()).add(player);
        }

        for (Language language : perLanguage.keySet()) {
            TextComponent[] tcs = new TextComponent[this.tcs.size()];

            int index = 0;
            for (TempTextComponent tc : this.tcs) {
                tcs[index] = tc.lang(language);
                index++;
            }

            TextComponent tc = new TextComponent(tcs);
            for (BungeePlayer player : perLanguage.get(language)) {
                player.getPlayer().sendMessage(tc);
            }
        }
    }

    private class TempTextComponent {

        private final Message component;
        private final ClickEvent.Action clickAction;
        private final Message clickEvent;
        private final HoverEvent.Action hoverAction;
        private final Message hoverEvent;

        public TempTextComponent(Message component, ClickEvent.Action clickAction, Message clickEvent, HoverEvent.Action hoverAction, Message hoverEvent) {
            this.component = component;
            this.clickAction = clickAction;
            this.clickEvent = clickEvent;
            this.hoverAction = hoverAction;
            this.hoverEvent = hoverEvent;
        }

        public TextComponent lang(Language language) {
            TextComponent tc = new TextComponent(component.lang(language));
            if (clickAction != null)
                tc.setClickEvent(new ClickEvent(clickAction, clickEvent.lang(language)));
            if (hoverAction != null)
                tc.setHoverEvent(new HoverEvent(hoverAction, new ComponentBuilder(hoverEvent.lang(language)).create()));

            return tc;
        }
    }
}
