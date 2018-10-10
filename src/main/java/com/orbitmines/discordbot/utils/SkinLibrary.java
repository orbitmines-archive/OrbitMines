package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.discordbot.DiscordBot;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkinLibrary {

    private static List<String> emoteCache = new ArrayList<>();

    public static String getSkinUrl(Type type, UUID uuid) {
        return type.url + uuid.toString() + ".png?time=" + System.currentTimeMillis();
    }

    public static void setupEmote(Guild guild, String name) {
        CachedPlayer player = CachedPlayer.getPlayer(name);

        UUID uuid = player != null ? player.getUUID() : UUIDUtils.getUUID(name);

        if (uuid == null)
            return;

        setupEmote(guild, uuid);
    }

    public static void setupEmote(Guild guild, UUID uuid) {
        if (emoteCache.contains(uuid.toString()))
            return;

        String name;

        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        if (player != null)
            name = player.getPlayerName();
        else
            name = UUIDUtils.getName(uuid);

        if (name == null)
            return;

        setupEmote(guild, uuid, name);
    }

    public static void setupEmote(Guild guild, UUID uuid, String name) {
        if (emoteCache.contains(uuid.toString()))
            return;

        deleteExistingEmotes(guild, uuid);

        try {
            guild.getController().createEmote("player_" + name, Icon.from(new URL(Type.HEAD_FLAT.url + uuid.toString()).openStream())).queue();
            emoteCache.add(uuid.toString());
        } catch (Exception e) {

        }
    }

    public static Emote getEmote(Guild guild, UUID uuid) {
        List<Emote> emotes = guild.getEmotesByName("player_" + CachedPlayer.getPlayer(uuid).getPlayerName(), true);
        return emotes.size() != 0 ? emotes.get(0) : DiscordBot.getInstance().getEmote(guild, DiscordBot.CustomEmote.unknown_player);
    }

    public static void deleteExistingEmotes(Guild guild, UUID uuid) {
        List<Emote> existing = guild.getEmotesByName("player_" + CachedPlayer.getPlayer(uuid).getPlayerName(), true);
        if (existing.size() != 0) {
            Emote emote = existing.get(0);
            emote.delete().queue();

            emoteCache.remove(uuid.toString());
        }
    }

    public static void deleteAllExistingEmotes(Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        for (Emote emote : new ArrayList<>(emotes)) {
            if (emote.getName().startsWith("player_"))
                emote.delete().queue();
        }

        emoteCache.clear();
    }

    public enum Type {

        HEAD_FLAT("https://crafatar.com/avatars/"),
        HEAD_3D("https://crafatar.com/renders/head/"),
        BODY_3D("https://crafatar.com/renders/body/");

        private final String url;

        Type(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
