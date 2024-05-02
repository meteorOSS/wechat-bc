package com.meteor.wechatbc.util;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class HttpUrlHelper {

    public static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

    public static String getValueByKey(Response response,String key){
        try {
            return getValueByKey(response.body().string(),key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValueByKey(String response, String key) {
        String[] keyValuePairs = response.split(";");
        for (String pair : keyValuePairs) {
            String[] parts = pair.trim().split("=", 2);
            if (parts.length == 2) {
                String currentKey = parts[0].trim();
                String value = parts[1].trim();
                if (key.equals(currentKey)) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        return value.substring(1, value.length() - 1);
                    } else {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    public static String generateTimestampWithRandom() {
        return System.currentTimeMillis() / 1000 + random(6);
    }

    public static String random(int count) {
        RandomString gen = new RandomString(count, new Random());
        return gen.nextString();
    }

    public static String encodeParams(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
        return encodedParams.toString();
    }
}
