package com.meteor.wechatbc.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.meteor.wechatbc.HttpAPI;
import com.meteor.wechatbc.entitiy.SendMessage;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckResponse;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.cookie.WeChatCookie;
import com.meteor.wechatbc.impl.interceptor.WeChatInterceptor;
import com.meteor.wechatbc.impl.model.Session;
import com.meteor.wechatbc.impl.model.WxInitInfo;
import com.meteor.wechatbc.util.HttpUrlHelper;
import com.meteor.wechatbc.util.URL;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120,TimeUnit.SECONDS)
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
            WxInitInfo wxInitInfo = JSON.parseObject(responseToString, WxInitInfo.class);
            session.setWxInitInfo(wxInitInfo);
            session.setCheckSyncKey(wxInitInfo.getSyncKey());
            session.setSyncKey(wxInitInfo.getSyncKey());
            Contact user = wxInitInfo.getUser();
            weChatClient.getLogger().info("已初始化微信信息:");
            weChatClient.getLogger().info("用户信息:");
            weChatClient.getLogger().info(user.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SyncCheckResponse syncCheck() {
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
                .addQueryParameter("synckey",weChatClient.getWeChatCore().getSession().getCheckSyncKey().toString())
                .build();
        Request request = BASE_REQUEST.newBuilder().url(httpUrl).get().build();
        try (
            Response response = okHttpClient.newCall(request).execute();
        ){
            String body = response.body().string();
            body = HttpUrlHelper.getValueByKey(body,"window.synccheck");
            JSONReader reader = JSONReader.of(body);
            reader.getContext().config(JSONReader.Feature.AllowUnQuotedFieldNames, true); // 允许未加引号的字段名
            JSONObject jsonObject = reader.read(JSONObject.class);
            return new SyncCheckResponse(jsonObject.getString("retcode"),jsonObject.getString("selector"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject getMessage() {
        Session session = weChatClient.getWeChatCore().getSession();
        BaseRequest baseRequest = session.getBaseRequest();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.WEBWXSYNC)
                .addQueryParameter("sid",baseRequest.getSid())
                .addQueryParameter("skey",baseRequest.getSkey())
                .addQueryParameter("pass_ticket",baseRequest.getPassTicket())
                .addQueryParameter("rr",String.valueOf(System.currentTimeMillis()))
                .build();

        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                .post(RequestBody.create(mediaType,JSON.toJSONString(session)))
                .build();

        try(
                Response response = okHttpClient.newCall(request).execute()
        ) {
            String body = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(body);
            return jsonObject;
        } catch (IOException e) {
            weChatClient.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject getContact() {
        Session session = weChatClient.getWeChatCore().getSession();
        BaseRequest baseRequest = session.getBaseRequest();
        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.GET_CONTACT)
                .addQueryParameter("skey",baseRequest.getSkey())
                .addQueryParameter("pass_ticket",baseRequest.getPassTicket())
                .addQueryParameter("rr",String.valueOf(System.currentTimeMillis()))
                .build();

        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                        .post(RequestBody.create(mediaType,JSONObject.toJSONString(new JSONObject())))
                                .build();

        try(
                Response response = okHttpClient.newCall(request).execute();
        ) {
            String body = response.body().string();
            return JSON.parseObject(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public JSONObject sendMessage(String toUserName,String content) {
        Session session = weChatClient.getWeChatCore().getSession();

        String s = HttpUrlHelper.generateTimestampWithRandom();

        SendMessage sendMessage = SendMessage.builder()
                .fromUserName(session.getWxInitInfo().getUser().getUserName())
                .localId(s)
                .clientMsgId(s)
                .content(content)
                .type(1)
                .toUserName(toUserName)
                .build();

        BaseRequest baseRequest = session.getBaseRequest();

        HttpUrl httpUrl = URL.BASE_URL.newBuilder()
                .encodedPath(URL.SEND_MESSAGE)
                .addQueryParameter("pass_ticket",baseRequest.getPassTicket())
                .addQueryParameter("lang","zh_CN")
                .build();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Msg",sendMessage);
        jsonObject.put("Scene",0);

        weChatClient.getLogger().debug(        jsonObject.toString());
        Request request = BASE_REQUEST.newBuilder().url(httpUrl)
                .post(RequestBody.create(mediaType,jsonObject.toString()))
                .build();

        try(Response response = okHttpClient.newCall(request).execute()) {
            String body = response.body().string();
            return JSON.parseObject(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
