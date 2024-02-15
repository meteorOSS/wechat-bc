package com.meteor.wechatbc.impl.model.message;

import com.meteor.wechatbc.entitiy.message.Message;

public class ImageEmoteMessage extends Message {
    @Override
    public String getContent() {
        return "(图片表情)";
    }
}
