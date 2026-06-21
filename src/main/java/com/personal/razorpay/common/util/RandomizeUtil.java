package com.personal.razorpay.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomizeUtil {

    // This class is being used because it does it's operations in a thread safe manner. TODO: Learn what that means from GPT
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String randomBase64(int length) {
        byte[] buf = new byte[length];
        SECURE_RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
