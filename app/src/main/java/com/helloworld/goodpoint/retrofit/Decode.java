package com.helloworld.goodpoint.retrofit;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Decode {
    private static String[] split;

    public static String decoded(String JWTEncoded) throws Exception {

            split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            Log.d("JWT_DECODED", "Signiture: " + getJson(split[2]));

        return getJson(split[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
