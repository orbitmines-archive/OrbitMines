package com.vexsotware.votifier.model;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VotifierEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Vote vote;

    public VotifierEvent(Vote vote) {
        this.vote = vote;
    }

    public Vote getVote() {
        return vote;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
