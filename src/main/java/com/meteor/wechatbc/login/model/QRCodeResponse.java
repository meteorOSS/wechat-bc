package com.meteor.wechatbc.login.model;

import com.meteor.wechatbc.util.HttpUrlHelper;
import lombok.Data;

/**
 * 扫码响应信息
 */
@Data
public class QRCodeResponse {
    private LoginMode loginMode;
    private String url;

    public QRCodeResponse(String data){
        String valueByKey = HttpUrlHelper.getValueByKey(data, "window.code");
        if(valueByKey.equalsIgnoreCase("201")){
            loginMode = LoginMode.LOGIN_MODE201;
        }else if(valueByKey.equalsIgnoreCase("408"))  loginMode = LoginMode.LOGIN_MODE408;
        else loginMode =  LoginMode.LOGIN_MODE200;
        this.url = HttpUrlHelper.getValueByKey(data,"window.redirect_uri");
    }
}
