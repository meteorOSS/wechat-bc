package com.meteor.wechatbc.util.login;

import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.util.login.cokkie.WeChatCookieJar;
import com.meteor.wechatbc.util.login.model.LoginMode;
import com.meteor.wechatbc.util.login.model.QRCodeResponse;
import com.meteor.wechatbc.util.BaseConfig;
import com.meteor.wechatbc.util.HttpUrlHelper;
import okhttp3.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.meteor.wechatbc.util.URL.*;

/**
 * 扫码登陆微信
 */
public class WeChatLogin {


    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new WeChatCookieJar())
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30,TimeUnit.MINUTES).
            build();

    private final HttpUrl.Builder urlBuilder = HttpUrl.parse(LOGINJS).newBuilder();

    private Logger logger;

    private boolean login;


    public WeChatLogin(Logger logger){
        this.logger = logger;
    }

    // 获取登陆UUID
    public String getLoginUUID(){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("mod", "desktop");

        HttpUrl redirectUrl = HttpUrl.parse(NEWLOGINPAGE)
                .newBuilder()
                .encodedQuery(HttpUrlHelper.encodeParams(queryParams)) // Encode query parameters
                .build();

        queryParams.clear();
        queryParams.put("redirect_uri", redirectUrl.toString());
        queryParams.put("appid", BaseConfig.APP_ID);
        queryParams.put("fun", "new");
        queryParams.put("lang", "zh_CN");
        queryParams.put("_", String.valueOf(System.currentTimeMillis()));

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .addHeader("User-Agent","Mozilla/5.0 (Linux; U; UOS x86_64; zh-cn) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 UOSBrowser/6.0.1.1001")
                .build();

        try {
            return HttpUrlHelper.getValueByKey(HttpUrlHelper.okHttpClient.newCall(request).execute(),"window.QRLogin.uuid");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查扫码状态
     */
    private QRCodeResponse checkLogin(String uuid, String tip){
        long now = System.currentTimeMillis() / 1000;
        String queryString = String.format("r=%d&_=%d&loginicon=true&uuid=%s&tip=%s",
                now / 1579, now, uuid, tip);
        String fullURL = LOGIN + "?" + queryString;
        Request request = new Request.Builder()
                .url(fullURL)
                .get().build();
        try(Response response = okHttpClient.newCall(request).execute()) {
            return new QRCodeResponse(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BaseRequest baseRequest = null;

    public BaseRequest getLoginInfo(){
        return baseRequest;
    }

    /**
     * 从Cookie中获取登录信息
     * @return
     */
    private BaseRequest getLoginInfo(QRCodeResponse qrCodeResponse) {
        if(baseRequest!=null) return baseRequest;
        Request request = new Request.Builder()
                .url(qrCodeResponse.getUrl() + "&fun=new")
                .addHeader("version", "2.0.0")
                .addHeader("extspam", BaseConfig.EXTSPAM)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try (Response execute = okHttpClient.newCall(request).execute()) {
            BaseRequest baseRequest = new BaseRequest(execute.body().string());
            List<Cookie> cookies = okHttpClient.cookieJar().loadForRequest(request.url());
            baseRequest.setInitCookie(cookies);
            // 设置登录设备ID
            baseRequest.setDeviceId(BaseConfig.getDeviceId());
            //  webwx_data_ticket ， webwx_auth_ticket需要从cookie中取得
            for (Cookie cookie : cookies) {
                String name = cookie.name();
                if (name.startsWith("webwx_data_ticket")) {
                    baseRequest.setDataTicket(cookie.value());
                } else if (name.startsWith("webwx_auth_ticket")) {
                    baseRequest.setAuthTicket(cookie.value());
                }
            }
            return baseRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 打印二维码
    public void login(){
        String uuid = getLoginUUID();
        String url = "https://login.weixin.qq.com/qrcode/"+uuid;
        System.out.println("访问: "+url+" 进行登录");
        // 等待登陆完成
        this.waitLogin(uuid);
    }

    public void waitLogin(String uuid){
        QRCodeResponse qrCodeResponse = null;
        while (!login){
            qrCodeResponse = checkLogin(uuid, "0");
            logger.info(qrCodeResponse.getLoginMode().getMsg());
            login = qrCodeResponse.getLoginMode()== LoginMode.LOGIN_MODE200;
        }
        // 获取登陆信息
        this.baseRequest = getLoginInfo(qrCodeResponse);
        if(baseRequest==null) {
            System.out.println("base request is null");
        }
        logger.info("已取得登录信息:");
        logger.info(baseRequest.toString());
    }


}
