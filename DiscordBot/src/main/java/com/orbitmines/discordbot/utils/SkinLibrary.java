package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import java.util.UUID;

public class SkinLibrary {

    public static String getSkinUrl(Type type, UUID uuid) {
        return type.url + uuid.toString();
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
