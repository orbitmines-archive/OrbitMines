package com.vexsotware.votifier;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.net.VotifierSession;
import io.netty.channel.Channel;

public abstract interface VoteHandler {

    public abstract void onVoteReceived(Channel paramChannel, Vote paramVote, VotifierSession.ProtocolVersion paramProtocolVersion) throws Exception;

    public abstract void onError(Channel paramChannel, Throwable paramThrowable);
}
