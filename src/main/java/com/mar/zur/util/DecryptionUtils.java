package com.mar.zur.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DecryptionUtils {

    private DecryptionUtils() {
    }

    public static String decryptStringBase64(String value) {
        try {
            return new String(Base64.getDecoder().decode(value), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to decrypt given message: " + value);
        }
    }

    public static String decryptStringROT13(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }

}
