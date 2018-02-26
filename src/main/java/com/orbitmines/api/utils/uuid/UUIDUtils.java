package com.orbitmines.api.utils.uuid;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class UUIDUtils {

    private static Map<String, UUID> cachedUUIDs = new HashMap<>();
    private static Map<UUID, String> cachedNames = new HashMap<>();

    public static String trim(UUID uuid) {
        return uuid.toString().replaceAll("-", "");
    }

    public static UUID parse(String trimmed) throws IndexOutOfBoundsException {
        return UUID.fromString(trimmed.substring(0, 8) + "-" + trimmed.substring(8, 12) + "-" + trimmed.substring(12, 16) + "-" + trimmed.substring(16, 20) + "-" + trimmed.substring(20, 32));
    }

    public static UUID getUUID(String playerName) {
        if (cachedUUIDs.containsKey(playerName))
            return cachedUUIDs.get(playerName);

        UUID uuid = getUUIDFromDatabase(playerName);
        if (uuid != null)
            return uuid;

        UUIDFetcher uuidFetcher = new UUIDFetcher(Collections.singletonList(playerName));
        try {
            uuid = uuidFetcher.call().get(playerName);
            if (uuid != null)
                cachedUUIDs.put(playerName, uuid);

            return uuid;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getName(UUID uuid) {
        if (!cachedNames.containsKey(uuid))
            return cachedNames.get(uuid);

        NameFetcher nameFetcher = new NameFetcher(uuid);
        try {
            String name = nameFetcher.call().get(uuid).get(nameFetcher.call().get(uuid).size() - 1);
            String[] nameParts = name.split(" ");

            cachedNames.put(uuid, nameParts[0]);
            return nameParts[0];
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getNameDate(UUID uuid) {
        NameFetcher nameFetcher = new NameFetcher(uuid);
        try {
            String name = nameFetcher.call().get(uuid).get(nameFetcher.call().get(uuid).size() - 1);
            String[] nameParts = name.split(" ", 1);
            return nameParts[1];
        } catch (Exception ex) {
            return null;
        }
    }

    public static Map<String, String> getNames(UUID uuid) {
        NameFetcher nameFetcher = new NameFetcher(uuid);
        try {
            Map<String, String> names = new HashMap<>();
            for (String name : nameFetcher.call().get(uuid)) {
                String[] nameParts = name.split(" ", 1);
                if (nameParts.length > 1) {
                    names.put(nameParts[0], nameParts[1]);
                } else {
                    names.put(nameParts[0], null);
                }
            }

            return names;
        } catch (Exception ex) {
            return null;
        }
    }

    public static UUID getUUIDFromDatabase(String playerName) {
        return Database.get().contains(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.NAME, playerName)) ? UUID.fromString(Database.get().getString(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.NAME, playerName))) : null;
    }
}
