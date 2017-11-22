package com.vexsoftware.votifier.bungee.events;

import com.vexsoftware.votifier.model.Vote;
import net.md_5.bungee.api.plugin.Event;

public class VotifierEvent extends Event {

    private final Vote vote;

    public VotifierEvent(Vote vote)
    {
        this.vote = vote;
    }

    public Vote getVote() {
        return vote;
    }
}
