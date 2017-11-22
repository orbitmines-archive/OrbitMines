package com.vexsoftware.votifier.net.protocol.v1crypto;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAIO {
    public RSAIO() {}

    public static void save(File directory, KeyPair keyPair) throws Exception {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKey.getEncoded());

        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        FileOutputStream publicOut = new FileOutputStream(directory + "/public.key");
        Throwable localThrowable6 = null;
        try {
            FileOutputStream privateOut = new FileOutputStream(directory + "/private.key");
            Throwable localThrowable7 = null;
            try {
                publicOut.write(DatatypeConverter.printBase64Binary(publicSpec.getEncoded())
                    .getBytes(StandardCharsets.UTF_8));
                privateOut.write(DatatypeConverter.printBase64Binary(privateSpec.getEncoded())
                    .getBytes(StandardCharsets.UTF_8));
            } catch (Throwable localThrowable1) {
                localThrowable7 = localThrowable1;
                throw localThrowable1;
            } finally {}
        } catch (Throwable localThrowable4) {
            localThrowable6 = localThrowable4;
            throw localThrowable4;
        } finally {
            if (publicOut != null) {
                if (localThrowable6 != null) {
                    try {
                        publicOut.close();
                    } catch (Throwable localThrowable5) {
                        localThrowable6.addSuppressed(localThrowable5);
                    }
                } else {
                    publicOut.close();
                }
            }
        }
    }

    public static KeyPair load(File directory) throws Exception {
        File publicKeyFile = new File(directory + "/public.key");
        byte[] encodedPublicKey = Files.readAllBytes(publicKeyFile.toPath());
        encodedPublicKey = DatatypeConverter.parseBase64Binary(new String(encodedPublicKey, StandardCharsets.UTF_8));

        File privateKeyFile = new File(directory + "/private.key");
        byte[] encodedPrivateKey = Files.readAllBytes(privateKeyFile.toPath());
        encodedPrivateKey = DatatypeConverter.parseBase64Binary(new String(encodedPrivateKey, StandardCharsets.UTF_8));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);

        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);

        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        return new KeyPair(publicKey, privateKey);
    }
}
