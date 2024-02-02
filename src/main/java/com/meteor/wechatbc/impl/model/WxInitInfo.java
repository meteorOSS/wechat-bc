package com.meteor.wechatbc.impl.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.meteor.wechatbc.entitiy.User;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import lombok.Data;

@Data
public class WxInitInfo {

    @JSONField(name = "SyncKey")
    private SyncKey syncKey;

    @JSONField(name = "User")
    private User user;

}
