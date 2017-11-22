package com.vexsoftware.votifier.bungee.forwarding.cache;

import com.vexsoftware.votifier.model.Vote;

import java.util.Collection;

public abstract interface VoteCache {

    public abstract Collection<String> getCachedServers();

    public abstract void addToCache(Vote paramVote, String paramString);

    public abstract Collection<Vote> evict(String paramString);
}
