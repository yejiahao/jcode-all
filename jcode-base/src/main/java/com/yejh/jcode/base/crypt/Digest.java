package com.yejh.jcode.base.crypt;

import java.security.MessageDigest;

/**
 * 摘要算法
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-11-09
 * @since 1.0.0
 */
public class Digest {

    private Digest() {
        throw new AssertionError();
    }

    public static String digest(String source) throws Exception {
        return digest(source, "MD5");
    }

    public static String digest(String source, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(source.getBytes());
        byte[] digest = md.digest();
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            sb.append(HEX.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String encodeStr = digest("中文English123");
        System.out.println("encodeStr: " + encodeStr);
    }
}
