package com.meteor.wechatbc.impl.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxInitInfo implements Serializable {

    @JSONField(name = "SyncKey")
    private SyncKey syncKey;

    @JSONField(name = "User")
    private Contact user;

}
