package com.meteor.wechatbc.util;

import com.alibaba.fastjson2.JSON;
import com.meteor.wechatbc.util.mode.GitHubRelease;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class VersionCheck {

    private static final String CURRENT_VERSION = "v1.1.2";

    public static void check(String owner, String repo) {

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/releases/latest";
        System.out.println("当前版本: "+CURRENT_VERSION);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            GitHubRelease gitHubRelease = JSON.parseObject(response.body().string(), GitHubRelease.class);
            if(!gitHubRelease.getTagName().equalsIgnoreCase(CURRENT_VERSION)){
                System.out.println("检测到新版本: "+gitHubRelease.getTagName());
                System.out.println("详情: ");
                System.out.println(gitHubRelease.getBody());
                System.out.println("地址: https://github.com/meteorOSS/wechat-bc/releases/latest");
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
