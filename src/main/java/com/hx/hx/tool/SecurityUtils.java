package com.hx.hx.tool;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.StringJoiner;
import java.util.UUID;

public class SecurityUtils {
    public static String generateNonce() {
        return UUID.randomUUID().toString().replace("-",  "");
    }

    public static String buildSignature(String secret, String... params) {
        StringJoiner raw = new StringJoiner(":");
        for (String param : params) {
            raw.add(param);
        }
        return DigestUtils.sha256Hex(secret  + ":" + raw.toString());
    }
}
