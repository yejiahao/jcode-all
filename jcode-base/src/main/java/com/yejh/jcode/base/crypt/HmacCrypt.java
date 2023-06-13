package com.yejh.jcode.base.crypt;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

public class HmacCrypt {

    private HmacCrypt() {
        throw new AssertionError();
    }

    public static String encrypt(String source, String key) throws GeneralSecurityException {
        return encrypt(source, key, "HmacSHA1");
    }

    public static String encrypt(String source, String key, String cryptType) throws GeneralSecurityException {
        // Get a hmac_sha1 key from the raw key bytes
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, cryptType);

        // Get a hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance(cryptType);
        mac.init(signingKey);

        // Compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(source.getBytes());

        //  Covert raw bytes to a String
        return Hex.encodeHexString(rawHmac);
    }

    public static void main(String[] args) throws GeneralSecurityException {
        System.out.println(encrypt("中文English123", "20190506"));
    }
}
