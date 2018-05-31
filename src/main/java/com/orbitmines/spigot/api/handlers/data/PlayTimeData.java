package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.Server;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.spigot.api.handlers.Data;

import java.util.*;

import static com.orbitmines.api.database.tables.TableVotes.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayTimeData extends Data {

    /* PlayTime is in seconds */
    private Map<Server, Long> playTime;

    public PlayTimeData(UUID uuid) {
        super(Table.PLAY_TIME, Type.PLAY_TIME, uuid);

        this.playTime = new HashMap<>();

        for (Server server : Server.values()) {
            this.playTime.put(server, 0L);
        }
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, new Column[] { UUID }, new Where(UUID, getUUID().toString()))) {
            /* So we don't have to add this manually; */
            ArrayList<String> list = new ArrayList<>();
            list.add(uuid.toString());
            for (int i = 0; i < Server.values().length; i++) {
                list.add("0");
            }

            Database.get().insert(table, list.toArray(new String[list.size()]));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Where(UUID, uuid.toString()));

            for (Server server : Server.values()) {
                this.playTime.put(server, Long.parseLong(values.get(server.getPlayTimeColumn())));
            }
        }
    }

    public Map<Server, Long> getPlayTime() {
        return playTime;
    }

    /* Following only used by Bungee */

    /* Current Session */
    private Server sessionServer;
    private long sessionStart;

    public void startSession(Server server) {
        stopSession();

        sessionServer = server;
        sessionStart = System.currentTimeMillis();
    }

    public void stopSession() {
        if (sessionServer == null)
            return;

        playTime.put(sessionServer, playTime.get(sessionServer) + ((System.currentTimeMillis() - sessionStart) / 1000));
        Database.get().update(Table.PLAY_TIME, new Set(sessionServer.getPlayTimeColumn(), playTime.get(sessionServer)), new Where(UUID, uuid.toString()));
    }
}
