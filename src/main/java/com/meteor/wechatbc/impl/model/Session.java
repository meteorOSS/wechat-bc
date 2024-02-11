package com.meteor.wechatbc.impl.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import lombok.Data;
import okhttp3.Cookie;

import java.util.List;

/**
 * Session会话信息，包含登录，请求信息
 */
@Data
public class Session {
    @JSONField(name = "BaseRequest")
    BaseRequest baseRequest;

    @JSONField(serialize = false)
    WxInitInfo wxInitInfo;

    @JSONField(serialize = false)
    List<Cookie> initCookie;

    // 作为同步消息的synckey，不参与序列化
    SyncKey checkSyncKey;

    // 获取消息的SyncKey
    @JSONField(name = "SyncKey")
    SyncKey syncKey;
}
