package com.vexsoftware.votifier.bungee.forwarding.cache;

import com.google.common.collect.ImmutableList;
import com.vexsoftware.votifier.model.Vote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryVoteCache implements VoteCache {

    protected final Map<String, Collection<Vote>> voteCache;

    public MemoryVoteCache(int initialSize) {
        voteCache = new HashMap(initialSize);
    }

    protected final ReentrantLock cacheLock = new ReentrantLock();

    public Collection<String> getCachedServers() {
        cacheLock.lock();
        try {
            return ImmutableList.copyOf(voteCache.keySet());
        } finally {
            cacheLock.unlock();
        }
    }

    public void addToCache(Vote v, String server) {
        if (server == null)
            throw new NullPointerException();

        cacheLock.lock();
        try {
            Collection<Vote> voteCollection = voteCache.get(server);
            if (voteCollection == null) {
                voteCollection = new ArrayList();
                voteCache.put(server, voteCollection);
            }
            voteCollection.add(v);
        } finally {
            cacheLock.unlock();
        }
    }

    public Collection<Vote> evict(String server) {
        if (server == null)
            throw new NullPointerException();

        cacheLock.lock();
        try {
            Collection<Vote> fromCollection = voteCache.remove(server);
            return fromCollection != null ? fromCollection : ImmutableList.of();
        } finally {
            cacheLock.unlock();
        }
    }
}
