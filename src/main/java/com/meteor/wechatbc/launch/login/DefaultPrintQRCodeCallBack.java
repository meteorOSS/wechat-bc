package com.meteor.wechatbc.launch.login;

import com.meteor.wechatbc.launch.login.PrintQRCodeCallBack;
import com.meteor.wechatbc.util.VersionCheck;

public class DefaultPrintQRCodeCallBack implements PrintQRCodeCallBack {
    @Override
    public String print(String uuid) {
        String url = "https://login.weixin.qq.com/qrcode/"+uuid;
        System.out.println("访问: "+url+" 进行登录!");
        return null;
    }
}
