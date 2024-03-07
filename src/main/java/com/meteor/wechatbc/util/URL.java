package com.meteor.wechatbc.util;

import lombok.Setter;
import okhttp3.HttpUrl;

/**
 * 微信相关接口
 */
public class URL {



    @Setter public static HttpUrl BASE_URL = new HttpUrl.Builder()
            .scheme("https")
            .host("wx.qq.com")
            .build();



    public final static String WXINIT = "/cgi-bin/mmwebwx-bin/webwxinit";
    public final static String SYNCCHECK = "/cgi-bin/mmwebwx-bin/synccheck";

    public final static String WEBWXSYNC = "/cgi-bin/mmwebwx-bin/webwxsync";

    public final static String SEND_MESSAGE = "/cgi-bin/mmwebwx-bin/webwxsendmsg";
    public final static String GET_CONTACT = "/cgi-bin/mmwebwx-bin/webwxgetcontact";
    public final static String BATCH_GET_CONTACT = "/cgi-bin/mmwebwx-bin/webwxbatchgetcontact";

    public final static String LOGINJS = "https://login.wx.qq.com/jslogin";
    public final static String NEWLOGINPAGE = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage";
    public final static String LOGIN = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login";

    public final static String GET_VIDEO = "/cgi-bin/mmwebwx-bin/webwxgetvideo";

    public final static String GET_VOICE = "/cgi-bin/mmwebwx-bin/webwxgetvoice";
    public final static String GET_MSG_IMG = "/cgi-bin/mmwebwx-bin/webwxgetmsgimg";
    public final static String REVOKE = "/cgi-bin/mmwebwx-bin/webwxrevokemsg";

    public final static String UPLOAD_FILE = "/cgi-bin/mmwebwx-bin/webwxuploadmedia";

    public final static String SEND_IMAGE = "/cgi-bin/mmwebwx-bin/webwxsendmsgimg";

    public final static String SEND_VIDEO = "/cgi-bin/mmwebwx-bin/webwxsendvideomsg";

    public final static String GET_ICON = "/cgi-bin/mmwebwx-bin/webwxgeticon";
}