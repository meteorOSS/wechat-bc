package com.meteor.wechatbc.impl.model.message;

import com.meteor.wechatbc.entitiy.message.Message;

public class VideoMessage extends Message {
    @Override
    public String getContent() {
        return "(视频消息)";
    }


}
