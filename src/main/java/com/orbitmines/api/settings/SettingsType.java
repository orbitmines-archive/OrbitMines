package com.orbitmines.api.settings;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;

public enum SettingsType {

    ENABLED(new Message("Iedereen", "Everyone"), Color.LIME),
    ONLY_FRIENDS(new Message("Alleen Vrienden", "Friends Only"), Color.AQUA),
    ONLY_FAVORITE_FRIENDS(new Message("Alleen Favoriete Vrienden", "Favorite Friends Only"), Color.ORANGE),
    DISABLED(new Message("Niemand", "Nobody"), Color.RED);

    private final Message name;
    private final Color color;

    SettingsType(Message name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Message getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getDisplayName(Language language) {
        return color.getChatColor() + name.lang(language);
    }

    public SettingsType next() {
        SettingsType[] values = SettingsType.values();

        return ordinal() == values.length - 1 ? values[0] : values[ordinal() + 1];
    }
}
