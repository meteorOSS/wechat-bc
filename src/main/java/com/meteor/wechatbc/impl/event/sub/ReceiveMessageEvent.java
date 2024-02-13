package com.meteor.wechatbc.impl.event.sub;

import com.meteor.wechatbc.entitiy.message.Message;

public class ReceiveMessageEvent extends MessageEvent{
    public ReceiveMessageEvent(Message message) {
        super(message);
    }
}
