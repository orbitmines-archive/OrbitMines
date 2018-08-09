package com.orbitmines.api.settings;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.tables.TablePlayers;

public enum Settings {

    PRIVATE_MESSAGES(new Message("Privéberichten", "Private Messages"), SettingsType.ENABLED) {
        @Override
        public Column getColumn() {
            return TablePlayers.SETTINGS_PRIVATE_MESSAGES;
        }
    },
    PLAYER_VISIBILITY(new Message("Speler Zichtbaarheid", "Player Visibility"), SettingsType.ENABLED) {
        @Override
        public Column getColumn() {
            return TablePlayers.SETTINGS_PLAYER_VISIBILITY;
        }
    },
    GADGETS(new Message("Gadgets"), SettingsType.ENABLED) {
        @Override
        public Column getColumn() {
            return TablePlayers.SETTINGS_GADGETS;
        }
    },
    STATS(new Message("Stats"), SettingsType.ONLY_FRIENDS) {
        @Override
        public Column getColumn() {
            return TablePlayers.SETTINGS_STATS;
        }
    };

    private final Message name;
    private final SettingsType defaultType;

    Settings(Message name, SettingsType defaultType) {
        this.name = name;
        this.defaultType = defaultType;
    }

    public Message getName() {
        return name;
    }

    public SettingsType getDefaultType() {
        return defaultType;
    }

    public Column getColumn() {
        throw new IllegalStateException();
    }
}
