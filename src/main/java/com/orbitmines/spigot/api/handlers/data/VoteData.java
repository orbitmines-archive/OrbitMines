package com.orbitmines.spigot.api.handlers.data;

import com.orbitmines.api.Server;
import com.orbitmines.api.ServerList;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.spigot.api.handlers.Data;

import java.util.*;
import java.util.UUID;

import static com.orbitmines.api.database.tables.TableVotes.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class VoteData extends Data {

    private int votes;
    private int totalVotes;
    private Map<Server, Integer> cachedVotes;
    private Map<ServerList, Long> voteTimeStamps;

    public VoteData(UUID uuid) {
        super(Table.VOTES, Type.VOTES, uuid);

        this.votes = 0;
        this.totalVotes = 0;
        this.cachedVotes = new HashMap<>();
        this.voteTimeStamps = new HashMap<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, UUID, new Where(UUID, uuid.toString()))) {
            Database.get().insert(table, Table.VOTES.values(uuid.toString(), votes + "", totalVotes + "", cachedVotes + "", serializeTimeStamps()));
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    VOTES,
                    TOTAL_VOTES,
                    CACHED_VOTES,
                    VOTE_TIME_STAMPS
            }, new Where(UUID, uuid.toString()));

            votes = Integer.parseInt(values.get(VOTES));
            totalVotes = Integer.parseInt(values.get(TOTAL_VOTES));

            String cachedVotes = values.get(CACHED_VOTES);
            updateCache(cachedVotes);

            String timeStamps = values.get(VOTE_TIME_STAMPS);
            updateVoteTimeStamps(timeStamps);
        }
    }

    public void addVote(ServerList serverList, Long timeStamp) {
        votes++;
        totalVotes++;

        for (Server server : Server.values()) {
            if (cachedVotes.containsKey(server))
                cachedVotes.put(server, cachedVotes.get(server) +1);
            else
                cachedVotes.put(server, 1);
        }

        voteTimeStamps.put(serverList, timeStamp);

        Database.get().update(table, new Set[] {

                new Set(VOTES, votes),
                new Set(TOTAL_VOTES, totalVotes),
                new Set(CACHED_VOTES, serializeCachedVotes()),
                new Set(VOTE_TIME_STAMPS, serializeTimeStamps())

        }, new Where(UUID, uuid.toString()));
    }

    public int getVotes() {
        return votes;
    }

    public Map<Server, Integer> getCachedVotes() {
        return cachedVotes;
    }

    private String serializeCachedVotes() {
        if (cachedVotes.size() == 0)
            return "null";

        StringBuilder stringBuilder = new StringBuilder();

        List<Server> keySet = new ArrayList<>(cachedVotes.keySet());

        for (int i = 0; i < keySet.size(); i++) {
            if (i != 0)
                stringBuilder.append("|");

            Server server = keySet.get(i);
            stringBuilder.append(server.toString()).append(":").append(cachedVotes.get(server));
        }

        return stringBuilder.toString();
    }

    public void updateCache() {
        updateCache(Database.get().getString(table, CACHED_VOTES, new Where(UUID, uuid.toString())));
    }

    private void updateCache(String cachedVotes) {
        if (!cachedVotes.equals("null")) {
            for (String serverData : cachedVotes.split("\\|")) {
                String[] cached = serverData.split(":");

                Server server;
                try {
                    server = Server.valueOf(cached[0]);
                } catch(IllegalArgumentException ex) {
                    continue;
                }

                this.cachedVotes.put(server, Integer.parseInt(cached[1]));
            }
        }
    }

    public Map<ServerList, Long> getVoteTimeStamps() {
        return voteTimeStamps;
    }

    public void updateVoteTimeStamps() {
        updateVoteTimeStamps(Database.get().getString(table, VOTE_TIME_STAMPS, new Where(UUID, uuid.toString())));
    }

    private void updateVoteTimeStamps(String timeStamps) {
        voteTimeStamps.clear();

        if (!timeStamps.equals("null")) {
            for (String voteTimeStampsSet : timeStamps.split(":")) {
                String[] voteTimeStampsData = voteTimeStampsSet.split("~");
                ServerList serverList = ServerList.fromDomain(voteTimeStampsData[0]);
                long timeStamp = Long.parseLong(voteTimeStampsData[1]);

                /* No need to add to time stamps if player can vote */
                if (!serverList.canVote(timeStamp))
                    voteTimeStamps.put(serverList, timeStamp);
            }
        }

        Database.get().update(table, new Set(VOTE_TIME_STAMPS, serializeTimeStamps()), new Where(UUID, getUUID().toString()));
    }

    private String serializeTimeStamps() {
        if (voteTimeStamps.size() == 0)
            return "null";

        StringBuilder stringBuilder = new StringBuilder();

        List<ServerList> keySet = new ArrayList<>(voteTimeStamps.keySet());

        for (int i = 0; i < keySet.size(); i++) {
            if (i != 0)
                stringBuilder.append(":");

            ServerList serverList = keySet.get(i);
            stringBuilder.append(serverList.getDomainName());
            stringBuilder.append("~");
            stringBuilder.append(voteTimeStamps.get(serverList));
        }

        return stringBuilder.toString();
    }
}
