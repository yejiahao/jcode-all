package com.yejh.jcode.base.crypt;

import java.util.Base64;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-12-13
 * @since 1.2.0
 */
public class Base64Crypt {

    public static String encrypt(String source) {
        byte[] target = Base64.getEncoder().encode(source.getBytes());
        return new String(target);
    }

    public static String decrypt(String source) {
        byte[] target = Base64.getDecoder().decode(source.getBytes());
        return new String(target);
    }

    public static void main(String[] args) {
        String encodeStr = encrypt("中文English123");
        String decodeStr = decrypt(encodeStr);
        System.out.println("encodeStr: " + encodeStr);
        System.out.println("decodeStr: " + decodeStr);
    }
}
