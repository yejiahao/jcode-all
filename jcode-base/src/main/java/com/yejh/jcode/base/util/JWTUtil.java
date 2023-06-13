package com.yejh.jcode.base.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Objects;

/**
 * 生成 jwt token
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-12-14
 * @since 2.0.0
 */
@Slf4j
public class JWTUtil {

    public static String getToken(String certPath, String keyID, String teamID) throws IOException {
        InputStreamReader is = new InputStreamReader(Objects.requireNonNull(JWTUtil.class.getClassLoader().getResourceAsStream(certPath)), StandardCharsets.UTF_8);
        ECPrivateKey privateKey = (ECPrivateKey) PemUtils.readPrivateKeyFromFileV2(is, "EC");
        Algorithm algorithm = Algorithm.ECDSA256(null, privateKey);
        // token 有效时间为 1h
        String token = JWT.create()
                .withHeader(ImmutableMap.of("alg", "ES256", "kid", keyID))
                .withPayload(ImmutableMap.of("iat", Instant.now().toEpochMilli() / 1000L, "iss", teamID))
                .sign(algorithm);
        log.info("token: {}", token);
        return token;
    }
}

class PemUtils {

    private static byte[] parsePEMFile(File pemFile) throws IOException {
        if (!pemFile.isFile() || !pemFile.exists()) {
            throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
        }
        PemReader reader = new PemReader(new FileReader(pemFile));
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        reader.close();
        return content;
    }

    private static byte[] parsePEMFileV2(InputStreamReader isr) throws IOException {
        if (Objects.isNull(isr)) {
            throw new FileNotFoundException();
        }
        PemReader reader = new PemReader(isr);
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        reader.close();
        return content;
    }

    private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) {
        PublicKey publicKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not reconstruct the public key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            System.out.println("Could not reconstruct the public key");
        }

        return publicKey;
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
        PrivateKey privateKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not reconstruct the private key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            System.out.println("Could not reconstruct the private key");
        }

        return privateKey;
    }

    public static PublicKey readPublicKeyFromFile(String filepath, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(new File(filepath));
        return PemUtils.getPublicKey(bytes, algorithm);
    }

    public static PrivateKey readPrivateKeyFromFile(String filepath, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(new File(filepath));
        return PemUtils.getPrivateKey(bytes, algorithm);
    }

    public static PrivateKey readPrivateKeyFromFileV2(InputStreamReader isr, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMFileV2(isr);
        return PemUtils.getPrivateKey(bytes, algorithm);
    }
}
