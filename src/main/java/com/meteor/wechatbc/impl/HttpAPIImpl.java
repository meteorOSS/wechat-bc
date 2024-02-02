package com.meteor.wechatbc.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.HttpAPI;
import com.meteor.wechatbc.impl.cookie.WeChatCookie;
import com.meteor.wechatbc.impl.interceptor.WeChatInterceptor;
import com.meteor.wechatbc.impl.model.Session;
import com.meteor.wechatbc.util.URL;
import okhttp3.*;

import java.io.IOException;

public class HttpAPIImpl implements HttpAPI {


    private WeChatClient weChatClient;
    private OkHttpClient okHttpClient;

    private WeChatInterceptor weChatInterceptor;

    private final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    private final Request BASE_REQUEST = new Request.Builder().addHeader("ContentType","application/json; charset=UTF-8").url(URL.BASE_URL).build();

    private WeChatCookie weChatCookie;

    public HttpAPIImpl(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
    }

    public void init(){
        this.weChatCookie = new WeChatCookie(weChatClient.getWeChatCore().getSession().getBaseRequest().getInitCookie());
        this.weChatInterceptor = new WeChatInterceptor(weChatClient.getWeChatCore());

        this.okHttpClient = new OkHttpClient.Builder()
                .cookieJar(this.weChatCookie) // cookie处理
                .addInterceptor(this.weChatInterceptor) // 拦截器
                .build();

    }

    @Override
    public void initWeChat() {
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.WXINIT)
                .addQueryParameter("_",String.valueOf(System.currentTimeMillis()))
                .build();

        Request request = BASE_REQUEST.newBuilder().url(httpUrl).post(RequestBody.create(mediaType,JSON.toJSONString(new JSONObject()))).build();

        try(
                Response response = okHttpClient.newCall(request).execute()
        ) {
            String responseToString = response.body().string();
            weChatClient.getLogger().info("已初始化微信:");
            weChatClient.getLogger().info(responseToString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
