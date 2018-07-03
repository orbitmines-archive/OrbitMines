package com.orbitmines.spigot.api.handlers.chat;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ComponentMessage {

    private List<TempTextComponent> tcs;

    public ComponentMessage() {
        tcs = new ArrayList<>();
    }

    public ComponentMessage add(Message component) {
        return add(component, null, null, null, null);
    }

    public ComponentMessage add(Message component, ClickEvent.Action clickAction, Message clickEvent) {
        return add(component, clickAction, clickEvent, null, null);
    }

    public ComponentMessage add(Message component, HoverEvent.Action hoverAction, Message hoverEvent) {
        return add(component, null, null, hoverAction, hoverEvent);
    }

    public ComponentMessage add(Message component, ClickEvent.Action clickAction, Message clickEvent, HoverEvent.Action hoverAction, Message hoverEvent) {
        tcs.add(new TempTextComponent(component, clickAction, clickEvent, hoverAction, hoverEvent));
        return this;
    }

    public void send(OMPlayer player) {
        send(Collections.singletonList(player));
    }

    public void send(OMPlayer... players) {
        send(Arrays.asList(players));
    }

    public void send(Collection<? extends OMPlayer> players) {
        Map<Language, List<OMPlayer>> perLanguage = new HashMap<>();
        for (OMPlayer player : players) {
            if (!perLanguage.containsKey(player.getLanguage()))
                perLanguage.put(player.getLanguage(), new ArrayList<>());

            perLanguage.get(player.getLanguage()).add(player);
        }

        for (Language language : perLanguage.keySet()) {
            TextComponent tc = getAsComponent(language);

            for (OMPlayer player : perLanguage.get(language)) {
                player.sendMessage(tc);
            }
        }
    }

    public TextComponent getAsComponent(Language language) {
        TextComponent[] tcs = new TextComponent[this.tcs.size()];

        int index = 0;
        for (TempTextComponent tc : this.tcs) {
            tcs[index] = tc.lang(language);
            index++;
        }

        return new TextComponent(tcs);
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
