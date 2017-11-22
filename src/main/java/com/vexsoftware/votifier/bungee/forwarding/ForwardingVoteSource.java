package com.vexsoftware.votifier.bungee.forwarding;

import com.vexsoftware.votifier.model.Vote;

public abstract interface ForwardingVoteSource {

    public abstract void forward(Vote paramVote);

    public abstract void halt();
}
