package com.meteor.wechatbc.entitiy.contact;

import com.alibaba.fastjson.annotation.JSONField;
import com.meteor.wechatbc.impl.WeChatClient;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class Contact {

    private WeChatClient weChatClient;

    @JSONField(name="Uin")
    private int uin;

    @JSONField(name="UserName")
    private String userName;

    @JSONField(name="NickName")
    private String nickName;

    @JSONField(name="HeadImgUrl")
    private String headImgUrl;

    @JSONField(name="ContactFlag")
    private int contactFlag;

    @JSONField(name="MemberCount")
    private int memberCount;

    @JSONField(name="MemberList")
    private List<?> memberList;

    @JSONField(name="RemarkName")
    private String remarkName;

    @JSONField(name="HideInputBarFlag")
    private int hideInputBarFlag;

    @JSONField(name="Sex")
    private int sex;

    @JSONField(name="Signature")
    private String signature;

    @JSONField(name="VerifyFlag")
    private int verifyFlag;

    @JSONField(name="OwnerUin")
    private int ownerUin;

    @JSONField(name="PYInitial")
    private String pyInitial;

    @JSONField(name="PYQuanPin")
    private String pyQuanPin;

    @JSONField(name="RemarkPYInitial")
    private String remarkPYInitial;

    @JSONField(name="RemarkPYQuanPin")
    private String remarkPYQuanPin;

    @JSONField(name="StarFriend")
    private int starFriend;

    @JSONField(name="AppAccountFlag")
    private int appAccountFlag;

    @JSONField(name="Statues")
    private int statues;

    @JSONField(name="AttrStatus")
    private long attrStatus;

    @JSONField(name="Province")
    private String province;

    @JSONField(name="City")
    private String city;

    @JSONField(name="Alias")
    private String alias;

    @JSONField(name="SnsFlag")
    private int snsFlag;

    @JSONField(name="UniFriend")
    private int uniFriend;

    @JSONField(name="DisplayName")
    private String displayName;

    @JSONField(name="ChatRoomId")
    private int chatRoomId;

    @JSONField(name="KeyWord")
    private String keyWord;

    @JSONField(name="EncryChatRoomId")
    private String encryChatRoomId;

    @JSONField(name="IsOwner")
    private int isOwner;
}
