package com.meteor.wechatbc.entitiy.contact;

import com.alibaba.fastjson.annotation.JSONField;
import com.meteor.wechatbc.entitiy.message.SentMessage;
import com.meteor.wechatbc.impl.HttpAPI;
import com.meteor.wechatbc.impl.WeChatClient;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.util.List;

@Data
@ToString
public class Contact {

    private transient WeChatClient weChatClient;

    @JSONField(name="Uin")
    private long uin;

    @JSONField(name="UserName")
    private String userName;

    @JSONField(name="NickName")
    private String nickName;

    @JSONField(name="HeadImgUrl")
    private String headImgUrl;

    @JSONField(name="ContactFlag")
    private long contactFlag;

    @JSONField(name="MemberCount")
    private long memberCount;

    @JSONField(name="MemberList")
    private List<?> memberList;

    @JSONField(name="RemarkName")
    private String remarkName;

    @JSONField(name="HideInputBarFlag")
    private long hideInputBarFlag;

    @JSONField(name="Sex")
    private long sex;

    @JSONField(name="Signature")
    private String signature;

    @JSONField(name="VerifyFlag")
    private long verifyFlag;

    @JSONField(name="OwnerUin")
    private long ownerUin;

    @JSONField(name="PYInitial")
    private String pyInitial;

    @JSONField(name="PYQuanPin")
    private String pyQuanPin;

    @JSONField(name="RemarkPYInitial")
    private String remarkPYInitial;

    @JSONField(name="RemarkPYQuanPin")
    private String remarkPYQuanPin;

    @JSONField(name="StarFriend")
    private long starFriend;

    @JSONField(name="AppAccountFlag")
    private long appAccountFlag;

    @JSONField(name="Statues")
    private long statues;

    @JSONField(name="AttrStatus")
    private long attrStatus;

    @JSONField(name="Province")
    private String province;

    @JSONField(name="City")
    private String city;

    @JSONField(name="Alias")
    private String alias;

    @JSONField(name="SnsFlag")
    private long snsFlag;

    @JSONField(name="UniFriend")
    private long uniFriend;

    @JSONField(name="DisplayName")
    private String displayName;

    @JSONField(name="ChatRoomId")
    private long chatRoomId;

    @JSONField(name="KeyWord")
    private String keyWord;

    @JSONField(name="EncryChatRoomId")
    private String encryChatRoomId;

    @JSONField(name="IsOwner")
    private long isOwner;

    protected HttpAPI httpAPI(){
        return weChatClient.getWeChatCore().getHttpAPI();
    }

    public SentMessage sendMessage(String message){
        return httpAPI().sendMessage(getUserName(),message);
    }

    public SentMessage sendImage(File file){
        return httpAPI().sendImage(getUserName(),file);
    }

    public SentMessage sendVideo(File file){
        return httpAPI().sendVideo(getUserName(),file);
    }

    /**
     * 判断是否为群聊
     * @return
     */
    public boolean isGroup(){
        return getUserName().startsWith("@@");
    }

    /**
     * 用户类型
     * @return
     */
    public ContactType getContactType(){
        return ContactType.from(this);
    }
}
