package com.orbitmines.spigot.api.handlers.chat;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class AdvancementMessage {

    private static long NEXT_ID = 0;

    private final OrbitMines orbitMines;

    private final NamespacedKey id;

    private String icon;
    private String title;

    private Frame frame;
    private boolean announce;
    private boolean popup;

    public AdvancementMessage(String icon, String title) {
        this.orbitMines = OrbitMines.getInstance();

        this.id = new NamespacedKey(orbitMines, "msd-" + 3 + String.format("%09d", NEXT_ID));
        NEXT_ID++;

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
        try {
            Bukkit.getUnsafe().loadAdvancement(id, serialize());
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
        }.runTaskLater(orbitMines, 100);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
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
            if (!player.isLoggedIn())
                continue;

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

    private String serialize() {
        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);

        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.addProperty("title", this.title);
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
