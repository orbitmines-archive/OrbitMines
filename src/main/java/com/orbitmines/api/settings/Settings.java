package com.orbitmines.api.settings;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.tables.TablePlayers;

public enum Settings {

    PRIVATE_MESSAGES(new Message("Privéberichten", "Private Messages"), TablePlayers.SETTINGS_PRIVATE_MESSAGES),
    PLAYER_VISIBILITY(new Message("Speler Zichtbaarheid", "Player Visibility"), TablePlayers.SETTINGS_PLAYER_VISIBILITY),
    GADGETS(new Message("Gadgets"), TablePlayers.SETTINGS_GADGETS);

    private final Message name;
    private final Column column;

    Settings(Message name, Column column) {
        this.name = name;
        this.column = column;
    }

    public Message getName() {
        return name;
    }

    public Column getColumn() {
        return column;
    }
}
