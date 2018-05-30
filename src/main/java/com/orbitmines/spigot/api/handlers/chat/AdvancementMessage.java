package com.orbitmines.spigot.api.handlers.chat;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class AdvancementMessage {

    private static long NEXT_ID = 0;

    private final OrbitMines plugin;

    private final NamespacedKey id;

    private String icon;
    private Message title;

    private Frame frame;
    private boolean announce;
    private boolean popup;

    public AdvancementMessage(String icon, Message title) {
        this.plugin = OrbitMines.getInstance();

        this.id = nextId();

        this.icon = icon;
        this.title = title;

        this.frame = Frame.GOAL;
        this.announce = false;
        this.popup = true;
    }

    public void send(OMPlayer player) {
        send(Collections.singletonList(player));
    }

    public void send(OMPlayer... players) {
        send(Arrays.asList(players));
    }

    public void send(Collection<? extends OMPlayer> players) {
        Map<Language, List<OMPlayer>> playersPerLanguage = new HashMap<>();

        for (OMPlayer player : players) {
            if (!playersPerLanguage.containsKey(player.getLanguage()))
                playersPerLanguage.put(player.getLanguage(), new ArrayList<>());

            playersPerLanguage.get(player.getLanguage()).add(player);
        }

        for (Language language : playersPerLanguage.keySet()) {
            send(playersPerLanguage.get(language), language);
        }
    }

    private void send(Collection<? extends OMPlayer> players, Language language) {
        try {
            Bukkit.getUnsafe().loadAdvancement(id, serialize(language));
            //DEBUG ConsoleUtils.success("Advancement " + id + " created!");
        } catch (IllegalArgumentException ex) {
            /* Already exists */
            ConsoleUtils.warn("Advancement " + id + " already exists!");
            return;
        }

        award(players);

        new BukkitRunnable() {
            @Override
            public void run() {
                revoke(players);
                Bukkit.getUnsafe().removeAdvancement(id);
            }
        }.runTaskLater(plugin, 100);
    }

    private NamespacedKey nextId() {
        NamespacedKey id = new NamespacedKey(plugin, "msd-" + 3 + String.format("%09d", NEXT_ID));
        NEXT_ID++;

        return id;
    }

    public AdvancementMessage copy() {
        AdvancementMessage copy = new AdvancementMessage(icon, title);
        copy.frame = this.frame;
        copy.announce = this.announce;
        copy.popup = this.popup;
        return copy;
    }

    public NamespacedKey getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Message getTitle() {
        return title;
    }

    public void setTitle(Message title) {
        this.title = title;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public boolean announce() {
        return announce;
    }

    public void setAnnounce(boolean announce) {
        this.announce = announce;
    }

    public boolean popup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    private void award(Collection<? extends OMPlayer> players) {
        Advancement advancement = Bukkit.getAdvancement(id);

        for (OMPlayer player : players) {
            AdvancementProgress progress = player.getPlayer().getAdvancementProgress(advancement);

            if (progress.isDone())
                continue;

            for (String criteria : progress.getRemainingCriteria()) {
                progress.awardCriteria(criteria);
            }
        }
    }

    private void revoke(Collection<? extends OMPlayer> players) {
        Advancement advancement = Bukkit.getAdvancement(id);

        for (OMPlayer player : players) {
            AdvancementProgress progress = player.getPlayer().getAdvancementProgress(advancement);

            if (!progress.isDone())
                continue;

            for (String criteria : progress.getAwardedCriteria()) {
                progress.revokeCriteria(criteria);
            }
        }
    }

    private String serialize(Language language) {
        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);

        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.addProperty("title", this.title.lang(language));
        display.addProperty("description", "");
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame", this.frame.toString());
        display.addProperty("announce_to_chat", this.announce);
        display.addProperty("show_toast", this.popup);
        display.addProperty("hidden", true);

        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();

        trigger.addProperty("trigger", "minecraft:impossible");
        criteria.add("impossible", trigger);

        json.add("criteria", criteria);
        json.add("display", display);

        return new GsonBuilder().setPrettyPrinting().create().toJson(json);
    }

    public enum Frame {

        GOAL("goal"),
        TASK("task"),
        CHALLENGE("challenge");

        private final String name;

        Frame(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
