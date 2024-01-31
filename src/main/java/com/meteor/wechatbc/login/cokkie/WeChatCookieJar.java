package com.meteor.wechatbc.login.cokkie;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在登陆的时候提取token存储
 */
public class WeChatCookieJar implements CookieJar {


    private Map<HttpUrl,List<Cookie>> cookieMap;

    public WeChatCookieJar(){
        this.cookieMap = new ConcurrentHashMap<>();
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        cookieMap.put(httpUrl,list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return cookieMap.getOrDefault(httpUrl,new ArrayList<>());
    }
}
