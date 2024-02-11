package com.meteor.wechatbc.entitiy.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class RecommendInfo {
    @JSONField(name = "UserName")
    private String userName;

    @JSONField(name = "NickName")
    private String nickName;

    @JSONField(name = "QQNum")
    private Integer qqNum;

    @JSONField(name = "Province")
    private String province;

    @JSONField(name = "City")
    private String city;

    @JSONField(name = "Content")
    private String content;

    @JSONField(name = "Signature")
    private String signature;

    @JSONField(name = "Alias")
    private String alias;

    @JSONField(name = "Scene")
    private Integer scene;

    @JSONField(name = "VerifyFlag")
    private Integer verifyFlag;

    @JSONField(name = "AttrStatus")
    private Integer attrStatus;

    @JSONField(name = "Sex")
    private Integer sex;

    @JSONField(name = "Ticket")
    private String ticket;

    @JSONField(name = "OpCode")
    private Integer opCode;
}
