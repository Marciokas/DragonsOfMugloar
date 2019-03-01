package com.mar.zur.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class EncryptionUtils {

    private EncryptionUtils(){}

    public static String decryptStringBase64(String value) {
        try {
            return new String(Base64.getDecoder().decode(value), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to decrypt given message: " + value);
        }
    }

}
