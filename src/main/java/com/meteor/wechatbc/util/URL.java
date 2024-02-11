package com.meteor.wechatbc.util;

import okhttp3.HttpUrl;

/**
 * 微信接口
 */
public class URL {

    public final static HttpUrl BASE_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("wx2.qq.com")
            .build();

    public final static String WXINIT = "/cgi-bin/mmwebwx-bin/webwxinit";
    public final static String SYNCCHECK = "/cgi-bin/mmwebwx-bin/synccheck";

    public final static String WEBWXSYNC = "/cgi-bin/mmwebwx-bin/webwxsync";

    public final static String LOGINJS = "https://login.wx.qq.com/jslogin";
    public final static String NEWLOGINPAGE = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage";
    public final static String LOGIN = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login";
}