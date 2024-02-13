package com.meteor.wechatbc.entitiy;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User0 {

    @JSONField(name = "Uin")
    private Long uin;

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
    private Integer hideInputBarFlag;

    @JSONField(name = "StarFriend")
    private Integer starFriend;

    @JSONField(name = "Sex")
    private Integer sex;

    @JSONField(name = "Signature")
    private String signature;

    @JSONField(name = "AppAccountFlag")
    private Integer appAccountFlag;

    @JSONField(name = "VerifyFlag")
    private Integer verifyFlag;

    @JSONField(name = "ContactFlag")
    private Integer contactFlag;

    @JSONField(name = "WebWxPluginSwitch")
    private Integer webWxPluginSwitch;

    @JSONField(name = "HeadImgFlag")
    private Integer headImgFlag;

    @JSONField(name = "SnsFlag")
    private Integer snsFlag;


}
