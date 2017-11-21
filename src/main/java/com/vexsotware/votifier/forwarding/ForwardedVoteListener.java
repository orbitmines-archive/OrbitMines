package com.vexsotware.votifier.forwarding;

import com.vexsoftware.votifier.model.Vote;

public abstract interface ForwardedVoteListener {

    public abstract void onForward(Vote paramVote);

}
