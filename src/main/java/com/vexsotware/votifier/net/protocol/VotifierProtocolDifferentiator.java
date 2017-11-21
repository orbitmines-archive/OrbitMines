package com.vexsotware.votifier.net.protocol;

import com.vexsoftware.votifier.net.VotifierSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class VotifierProtocolDifferentiator extends ByteToMessageDecoder {

    private static final short PROTOCOL_2_MAGIC = 29498;
    private final boolean testMode;
    private final boolean allowv1;

    public VotifierProtocolDifferentiator(boolean testMode, boolean allowv1) {
        this.testMode = testMode;
        this.allowv1 = allowv1;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        int readable = buf.readableBytes();

        if (readable < 2) {
            return;
        }

        short readMagic = buf.readShort();


        buf.readerIndex(0);

        VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();

        if (readMagic == 29498) {
            session.setVersion(VotifierSession.ProtocolVersion.TWO);

            if (!testMode) {
                ctx.pipeline().addAfter("protocolDifferentiator", "protocol2LengthDecoder", new LengthFieldBasedFrameDecoder(1024, 2, 2, 0, 4));
                ctx.pipeline().addAfter("protocol2LengthDecoder", "protocol2StringDecoder", new StringDecoder(StandardCharsets.UTF_8));
                ctx.pipeline().addAfter("protocol2StringDecoder", "protocol2VoteDecoder", new VotifierProtocol2Decoder());
                ctx.pipeline().addAfter("protocol2VoteDecoder", "protocol2StringEncoder", new StringEncoder(StandardCharsets.UTF_8));
                ctx.pipeline().remove(this);
            }
        } else {
            if (!allowv1) {
                throw new CorruptedFrameException("This server only accepts well-formed Votifier v2 packets.");
            }

            session.setVersion(VotifierSession.ProtocolVersion.ONE);
            if (!testMode) {
                ctx.pipeline().addAfter("protocolDifferentiator", "protocol1Handler", new VotifierProtocol1Decoder());
                ctx.pipeline().remove(this);
            }
        }
    }
}
