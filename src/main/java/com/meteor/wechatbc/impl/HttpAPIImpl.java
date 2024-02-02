package com.meteor.wechatbc.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.HttpAPI;
import com.meteor.wechatbc.entitiy.SyncCheckSelector;
import com.meteor.wechatbc.entitiy.User;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.cookie.WeChatCookie;
import com.meteor.wechatbc.impl.interceptor.WeChatInterceptor;
import com.meteor.wechatbc.impl.model.Session;
import com.meteor.wechatbc.impl.model.WxInitInfo;
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
            Session session = weChatClient.getWeChatCore().getSession();
            session.setWxInitInfo(JSON.parseObject(responseToString, WxInitInfo.class));
            WxInitInfo wxInitInfo = session.getWxInitInfo();
            User user = wxInitInfo.getUser();
            weChatClient.getLogger().info("已初始化微信信息:");
            weChatClient.getLogger().info("用户信息:");
            weChatClient.getLogger().info(user.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SyncCheckSelector syncCheck() {
        Session session = weChatClient.getWeChatCore().getSession();
        BaseRequest baseRequest = session.getBaseRequest();

        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.SYNCCHECK)
                .addQueryParameter("r",String.valueOf(System.currentTimeMillis()))
                .addQueryParameter("skey",baseRequest.getSkey())
                .addQueryParameter("sid",baseRequest.getSid())
                .addQueryParameter("uin",baseRequest.getUin())
                .addQueryParameter("deviceid",baseRequest.getDeviceId())
                .addQueryParameter("_",String.valueOf(System.currentTimeMillis()))
                .build();
        return null;
    }


}
