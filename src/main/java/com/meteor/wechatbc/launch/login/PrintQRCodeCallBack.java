package com.meteor.wechatbc.launch.login;

import java.util.UUID;

/**
 * 打印登录二维码的回调
 */
public interface PrintQRCodeCallBack {
    String print(String uuid);
}
