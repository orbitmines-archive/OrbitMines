package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.CachedPlayer;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkinLibrary {

    private static List<UUID> emoteCache = new ArrayList<>();

    public static String getSkinUrl(Type type, UUID uuid) {
        return type.url + uuid.toString();
    }

    public static void setupEmote(Guild guild, UUID uuid) {
        CachedPlayer player = CachedPlayer.getPlayer(uuid);

        if (emoteCache.contains(uuid))
            return;

        List<Emote> existing = guild.getEmotesByName("player_" + player.getPlayerName(), true);
        if (existing.size() != 0) {
            Emote emote = existing.get(0);
            emote.delete().queue();
        }

        try {
            guild.getController().createEmote("player_" + player.getPlayerName(), Icon.from(new URL(Type.HEAD_FLAT.url + uuid.toString()).openStream())).queue();
            emoteCache.add(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Emote getEmote(Guild guild, UUID uuid) {
        return guild.getEmotesByName("player_" + CachedPlayer.getPlayer(uuid).getPlayerName(), true).get(0);
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
