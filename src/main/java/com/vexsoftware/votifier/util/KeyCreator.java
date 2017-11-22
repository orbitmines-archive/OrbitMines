package com.vexsoftware.votifier.util;

import java.nio.charset.StandardCharsets;

public class KeyCreator {

    public KeyCreator() {}

    public static java.security.Key createKeyFrom(String token) {
        return new javax.crypto.spec.SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }
}
