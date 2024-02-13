package com.meteor.wechatbc.impl.event.sub;

import com.meteor.wechatbc.entitiy.message.Message;

public class OwnerMessageEvent extends MessageEvent{
    public OwnerMessageEvent(Message message) {
        super(message);
    }
}
