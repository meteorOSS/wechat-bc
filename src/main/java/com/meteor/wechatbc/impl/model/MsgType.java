package com.meteor.wechatbc.impl.model;

public enum MsgType {
    TextMsg(1), //文本消息
    ImgEmoteMsg(47), // 图片表情
    ImageMsg(3); // 图片消息


    /**
     * 未处理的消息

    VoiceMsg(34), // 语音消息
    PossibleFriendMsg(40), // 好友推荐消息
    ContactCardMsg(42), // 名片消息
    VideoMsg(43), // 视频消息
    RecalledMsg(10002); // 撤回消息

     **/

    private int idx;
    MsgType(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public static MsgType fromIdx(int idx){
        for (MsgType value : MsgType.values()) {
            if(value.getIdx() == idx) return value;
        }
        return TextMsg;
    }
}
