package com.orbitmines.spigot.api.handlers.data;

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
    private int cachedVotes;
    private Map<ServerList, Long> voteTimeStamps;

    public VoteData(UUID uuid) {
        super(Table.VOTES, Type.VOTES, uuid);

        this.votes = 0;
        this.totalVotes = 0;
        this.cachedVotes = 0;
        this.voteTimeStamps = new HashMap<>();
    }

    @Override
    public void load() {
        if (!Database.get().contains(table, UUID, new Where(UUID, uuid.toString()))) {
            Database.get().insert(table, uuid.toString(), votes + "", totalVotes + "", cachedVotes + "", serializeTimeStamps());
        } else {
            Map<Column, String> values = Database.get().getValues(table, new Column[] {
                    VOTES,
                    TOTAL_VOTES,
                    CACHED_VOTES,
                    VOTE_TIME_STAMPS
            }, new Where(UUID, uuid.toString()));

            votes = Integer.parseInt(values.get(VOTES));
            totalVotes = Integer.parseInt(values.get(TOTAL_VOTES));
            cachedVotes = Integer.parseInt(values.get(CACHED_VOTES));

            String timeStamps = values.get(VOTE_TIME_STAMPS);
            updateVoteTimeStamps(timeStamps);
        }
    }

    public void addVote(ServerList serverList, Long timeStamp) {
        votes++;
        totalVotes++;
        cachedVotes++;

        voteTimeStamps.put(serverList, timeStamp);

        Database.get().update(table, new Set[] {

                new Set(VOTES, votes),
                new Set(TOTAL_VOTES, totalVotes),
                new Set(CACHED_VOTES, cachedVotes),
                new Set(VOTE_TIME_STAMPS, serializeTimeStamps())

        }, new Where(UUID, uuid.toString()));
    }

    public int getVotes() {
        return votes;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public int getCachedVotes() {
        return cachedVotes;
    }

    public void clearCache() {
        cachedVotes = 0;
        Database.get().update(table, new Set(CACHED_VOTES, cachedVotes), new Where(UUID, uuid.toString()));
    }

    public void updateCache() {
        cachedVotes = Database.get().getInt(table, CACHED_VOTES, new Where(UUID, uuid.toString()));
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
