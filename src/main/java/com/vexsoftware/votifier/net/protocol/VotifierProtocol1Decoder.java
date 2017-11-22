package com.vexsoftware.votifier.net.protocol;

import com.vexsoftware.votifier.VotifierPlugin;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.net.protocol.v1crypto.RSA;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class VotifierProtocol1Decoder extends ByteToMessageDecoder {

    public VotifierProtocol1Decoder() {}

    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readableBytes() < 256) {
            return;
        }

        byte[] block = new byte[buf.readableBytes()];
        buf.getBytes(0, block);

        buf.readerIndex(buf.readableBytes());

        VotifierPlugin plugin = ctx.channel().attr(VotifierPlugin.KEY).get();
        try {
            block = RSA.decrypt(block, plugin.getProtocolV1Key().getPrivate());
        } catch (Exception e) {
            throw new CorruptedFrameException("Could not decrypt data (is your key correct?)", e);
        }

        String all = new String(block, StandardCharsets.US_ASCII);
        String[] split = all.split("\n");
        if (split.length < 5) {
            throw new CorruptedFrameException("Not enough fields specified in vote.");
        }

        if (!split[0].equals("VOTE")) {
            throw new CorruptedFrameException("VOTE opcode not found");
        }

        Vote vote = new Vote(split[1], split[2], split[3], split[4]);
        list.add(vote);

        ctx.pipeline().remove(this);
    }
}
