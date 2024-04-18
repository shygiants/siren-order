package io.github.shygiants.sirenorder.utils;

import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@RequiredArgsConstructor
public class KeyPairLoader {
    private final String privateKeyPath;
    private final String publicKeyPath;

    private static byte[] readFromPath(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] keyBytes = new byte[fis.available()];
        fis.read(keyBytes);
        fis.close();
        return keyBytes;
    }

    private PrivateKey readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = readFromPath(privateKeyPath);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey readPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = readFromPath(publicKeyPath);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public KeyPair load() {
        try {
            PrivateKey privateKey = readPrivateKey();
            PublicKey publicKey = readPublicKey();
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
