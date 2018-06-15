package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.database.*;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.spigot.api.handlers.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.orbitmines.api.database.tables.TablePlayers.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SettingsData extends Data {

    private Map<Settings, SettingsType> settings;

    public SettingsData(UUID uuid) {
        super(Table.PLAYERS, Type.SETTINGS, uuid);

        this.settings = new HashMap<>();

        for (Settings settings : Settings.values()) {
            this.settings.put(settings, SettingsType.ENABLED);
        }
    }

    @Override
    public void load() {
        Map<Column, String> values = Database.get().getValues(table, new Column[] {
                SETTINGS_PRIVATE_MESSAGES,
                SETTINGS_PLAYER_VISIBILITY,
                SETTINGS_GADGETS,
        }, new Where(UUID, uuid.toString()));

        for (Settings settings : Settings.values()) {
            this.settings.put(settings, SettingsType.valueOf(values.get(settings.getColumn())));
        }
    }

    public Map<Settings, SettingsType> getSettings() {
        return settings;
    }

    public void setSettings(Settings settings, SettingsType settingsType) {
        this.settings.put(settings, settingsType);

        Database.get().update(table, new Set(settings.getColumn(), settingsType.toString()), new Where(UUID, uuid.toString()));
    }
}
