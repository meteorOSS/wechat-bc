package com.meteor.wechatbc.impl.interceptor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.impl.WeChatCoreImpl;
import com.meteor.wechatbc.impl.model.Session;
import com.meteor.wechatbc.util.URL;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 拦截器
 * 进行BaseRequest公参的添加
 */
public class WeChatInterceptor implements Interceptor {

    private WeChatCoreImpl weChatCore;

    public WeChatInterceptor(WeChatCoreImpl weChatCore){
        this.weChatCore = weChatCore;
    }

    private String bodyToString(final RequestBody body) {
        try (Buffer buffer = new Buffer()) {
            if (body != null) {
                body.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (IOException e) {
            return "Did not work";
        }
    }

    // 不进行拦截操作的url名单
    private static List<String> BLACK_URL
            = Arrays.asList(URL.UPLOAD_FILE);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // 如果在黑名单里不进行任何操作
        if(BLACK_URL.contains(originalRequest.url().encodedPath())) return chain.proceed(chain.request());


        // 仅拦截post请求
        if(!originalRequest.method().equalsIgnoreCase("POST")) return chain.proceed(chain.request());

        Session session = weChatCore.getSession();
        RequestBody originalBody = originalRequest.body();

        MediaType mediaType = originalBody.contentType();


        // 请求体转换为JSON对象
        JSONObject originalJson = JSON.parseObject(bodyToString(originalBody), JSONObject.class);

        // 添加公共BaseRequest
        originalJson.put("BaseRequest", JSON.toJSONString(session.getBaseRequest()));



        Request request = chain.request();



        if(request.url().encodedPath().equalsIgnoreCase(URL.WEBWXSYNC)){
            originalJson.put("rr",String.valueOf(-new Date().getTime() / 1000));
        }

        RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(originalJson));

        // 构建新的请求
        Request newRequest = originalRequest.newBuilder()
                .method(originalRequest.method(), requestBody)
                .build();

        return chain.proceed(newRequest);
    }




}
