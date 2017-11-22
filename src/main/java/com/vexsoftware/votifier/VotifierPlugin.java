package com.vexsoftware.votifier;

import io.netty.util.AttributeKey;

import java.security.Key;
import java.security.KeyPair;
import java.util.Map;

public abstract interface VotifierPlugin {

    public static final AttributeKey<VotifierPlugin> KEY = AttributeKey.valueOf("votifier_plugin");

    public abstract Map<String, Key> getTokens();

    public abstract KeyPair getProtocolV1Key();

    public abstract String getVersion();
}
