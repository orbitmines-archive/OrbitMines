package com.vexsotware.votifier.bungee.forwarding.proxy.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class VotifierProtocol2Encoder extends MessageToByteEncoder<VoteRequest> {

    private static final short MAGIC = 29498;
    private final Key key;

    public VotifierProtocol2Encoder(Key key) {
        this.key = key;
    }

    protected void encode(ChannelHandlerContext ctx, VoteRequest req, ByteBuf buf) throws Exception {
        JSONObject object = new JSONObject();
        JSONObject payloadObject = req.getVote().serialize();
        payloadObject.put("challenge", req.getChallenge());
        String payload = payloadObject.toString();
        object.put("payload", payload);


        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        mac.update(payload.getBytes(StandardCharsets.UTF_8));
        String computed = DatatypeConverter.printBase64Binary(mac.doFinal());
        object.put("signature", computed);


        String finalMessage = object.toString();
        buf.writeShort(29498);
        buf.writeShort(finalMessage.length());
        ByteBuf messageBytes = Unpooled.copiedBuffer(finalMessage, StandardCharsets.UTF_8);
        buf.writeBytes(messageBytes);
        messageBytes.release();
    }
}
