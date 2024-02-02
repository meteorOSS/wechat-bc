package com.meteor.wechatbc.entitiy;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class User {

    @JSONField(name = "Uin")
    private long uin;

    @JSONField(name = "UserName")
    private String userName;

    @JSONField(name = "NickName")
    private String nickName;

    @JSONField(name = "HeadImgUrl")
    private String headImgUrl;

    @JSONField(name = "RemarkName")
    private String remarkName;

    @JSONField(name = "PYInitial")
    private String pyInitial;

    @JSONField(name = "PYQuanPin")
    private String pyQuanPin;

    @JSONField(name = "RemarkPYInitial")
    private String remarkPYInitial;

    @JSONField(name = "RemarkPYQuanPin")
    private String remarkPYQuanPin;

    @JSONField(name = "HideInputBarFlag")
    private int hideInputBarFlag;

    @JSONField(name = "StarFriend")
    private int starFriend;

    @JSONField(name = "Sex")
    private int sex;

    @JSONField(name = "Signature")
    private String signature;

    @JSONField(name = "AppAccountFlag")
    private int appAccountFlag;

    @JSONField(name = "VerifyFlag")
    private int verifyFlag;

    @JSONField(name = "ContactFlag")
    private int contactFlag;

    @JSONField(name = "WebWxPluginSwitch")
    private int webWxPluginSwitch;

    @JSONField(name = "HeadImgFlag")
    private int headImgFlag;

    @JSONField(name = "SnsFlag")
    private int snsFlag;
}
