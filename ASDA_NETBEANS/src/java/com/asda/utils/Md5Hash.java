package com.asda.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Felipe Osorio Thomé
 */
public abstract class Md5Hash {

    public static String generator(byte[] message) {
        String hash = null;
        
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(message);
            hash = toHex(md5.digest());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Unable to calculate MD5 Digests" + "\n" + ex.toString());
        }
        
        return hash;
    }

    private static String toHex(byte[] digest) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            buf.append(Integer.toHexString((int) digest[i] & 0x00ff));
        }
        return buf.toString();
    }
}
