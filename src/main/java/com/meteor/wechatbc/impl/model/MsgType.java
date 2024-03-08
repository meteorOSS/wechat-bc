package com.meteor.wechatbc.impl.model;

import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.impl.model.message.*;

public enum MsgType{
    TextMsg("1"), //文本消息
    ImgEmoteMsg("47"), // 图片表情
    ImageMsg("3"),// 图片消息
    VideoMsg("43"), // 视频消息
    APPMsg("49"), // APP消息
    SysMsg("10000"), // 系统消息
    VoiceMsg("34"), // 语音消息

    RevokeMsg("10002"); // 撤回消息

    /**
     * 未处理的消息
     PossibleFriendMsg(40), // 好友推荐消息
     ContactCardMsg(42), // 名片消息
     RecalledMsg(10002); // 撤回消息
     **/

    private String idx;


    MsgType(String idx) {
        this.idx = idx;
    }

    public String getIdx() {
        return idx;
    }

    public static MsgType fromIdx(String idx){
        System.out.println("msgid: "+idx);
        for (MsgType value : MsgType.values()) {
            if(value.getIdx().equalsIgnoreCase(idx)) return value;
        }
        return TextMsg;
    }
}
