package com.orbitmines.bungeecord.handlers.chat;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import net.md_5.bungee.api.ChatColor;
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
    public ComponentMessage(ComponentMessage componentMessage) {
        this.tcs = new ArrayList<>(componentMessage.tcs);
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

    public ComponentMessage add(TempTextComponent textComponent) {
        tcs.add(textComponent);
        return this;
    }

    public List<TempTextComponent> getComponents() {
        return tcs;
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

    public static class TempTextComponent {


        private Message component;
        private final ClickEvent.Action clickAction;
        private Message clickEvent;
        private final HoverEvent.Action hoverAction;
        private Message hoverEvent;

        private ChatColor chatColor;
        private boolean bold;
        private boolean italic;
        private boolean obfuscated;
        private boolean strikethrough;
        private boolean underlined;

        public TempTextComponent(String component) {
            this(new Message(component), null, null, null, null);
        }

        public TempTextComponent(Message component) {
            this(component, null, null, null, null);
        }

        public TempTextComponent(String component, ClickEvent.Action clickAction, String clickEvent) {
            this(new Message(component), clickAction, new Message(clickEvent), null, null);
        }

        public TempTextComponent(Message component, ClickEvent.Action clickAction, Message clickEvent) {
            this(component, clickAction, clickEvent, null, null);
        }

        public TempTextComponent(String component, HoverEvent.Action hoverAction, String hoverEvent) {
            this(new Message(component), null, null, hoverAction, new Message(hoverEvent));
        }

        public TempTextComponent(Message component, HoverEvent.Action hoverAction, Message hoverEvent) {
            this(component, null, null, hoverAction, hoverEvent);
        }

        public TempTextComponent(String component, ClickEvent.Action clickAction, String clickEvent, HoverEvent.Action hoverAction, String hoverEvent) {
            this(new Message(component), clickAction, new Message(clickEvent), hoverAction, new Message(hoverEvent));
        }

        public TempTextComponent(Message component, ClickEvent.Action clickAction, Message clickEvent, HoverEvent.Action hoverAction, Message hoverEvent) {
            this.component = component;
            this.clickAction = clickAction;
            this.clickEvent = clickEvent;
            this.hoverAction = hoverAction;
            this.hoverEvent = hoverEvent;
            this.chatColor = ChatColor.WHITE;
            this.bold = false;
            this.italic = false;
            this.obfuscated = false;
            this.strikethrough = false;
            this.underlined = false;
        }

        public TempTextComponent setComponent(Message component) {
            this.component = component;
            return this;
        }

        public TempTextComponent setClickEvent(Message clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        public TempTextComponent setHoverEvent(Message hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public TempTextComponent setChatColor(ChatColor chatColor) {
            this.chatColor = chatColor;
            return this;
        }

        public TempTextComponent setBold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public TempTextComponent setItalic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public TempTextComponent setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public TempTextComponent setStrikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public TempTextComponent setUnderlined(boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public TextComponent lang(Language language) {
            TextComponent tc = new TextComponent(component.lang(language));
            if (clickAction != null)
                tc.setClickEvent(new ClickEvent(clickAction, clickEvent.lang(language)));
            if (hoverAction != null)
                tc.setHoverEvent(new HoverEvent(hoverAction, new ComponentBuilder(hoverEvent.lang(language)).create()));

            tc.setColor(chatColor);
            tc.setBold(bold);
            tc.setItalic(italic);
            tc.setObfuscated(obfuscated);
            tc.setStrikethrough(strikethrough);
            tc.setUnderlined(underlined);

            return tc;
        }
    }
}
