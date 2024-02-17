package com.meteor.wechatbc.impl.cookie;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeChatCookie implements CookieJar {

    // 登陆后初始化的Cookie
    private List<Cookie> initCookie;
    private Map<HttpUrl,List<Cookie>> cookieListMap;

    public WeChatCookie(List<Cookie> initCookie){
        this.initCookie = initCookie;
        this.cookieListMap = new HashMap<>();
    }


    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        cookieListMap.put(httpUrl,list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return cookieListMap.getOrDefault(httpUrl,initCookie);
    }
}
