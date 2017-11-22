package com.vexsoftware.votifier.util.standalone;

import com.vexsoftware.votifier.model.Vote;

public abstract interface VoteReceiver {

    public abstract void onVote(Vote paramVote) throws Exception;

    public abstract void onException(Throwable paramThrowable);
}
