package com.vexsotware.votifier.bungee.forwarding.proxy.client;

import com.madblock.bungeecord.MadBlockBungee;
import com.vexsoftware.votifier.model.Vote;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.CorruptedFrameException;

public class VotifierProtocol2HandshakeHandler extends SimpleChannelInboundHandler<String> {

    private final Vote toSend;
    private final VotifierResponseHandler responseHandler;
    private final MadBlockBungee nuVotifier;

    public VotifierProtocol2HandshakeHandler(Vote toSend, VotifierResponseHandler responseHandler, MadBlockBungee nuVotifier) {
        this.toSend = toSend;
        this.responseHandler = responseHandler;
        this.nuVotifier = nuVotifier;
    }

    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        String[] handshakeContents = s.split(" ");
        if (handshakeContents.length != 3) {
            throw new CorruptedFrameException("Handshake is not valid.");
        }

        VoteRequest request = new VoteRequest(handshakeContents[2], toSend);
        if (nuVotifier.isDebug()) {
            nuVotifier.getLogger().info("Sent request: " + request.toString());
        }
        ctx.writeAndFlush(request);
        ctx.pipeline().addLast(new VotifierProtocol2ResponseHandler(responseHandler));
        ctx.pipeline().remove(this);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        responseHandler.onFailure(cause);
        ctx.close();
    }
}
