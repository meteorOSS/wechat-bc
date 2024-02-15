package com.meteor.wechatbc.impl.synccheck.message;

import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.impl.WeChatClient;

/**
 * 消息工厂
 * 用于将原始message转换为 图片消息，文本消息等
 */
public class MessageProcessor {

    private final ConcreteMessageFactory concreteMessageFactory;

    public MessageProcessor(WeChatClient weChatClient){
        this.concreteMessageFactory = new ConcreteMessageFactory(weChatClient);
    }


    public Message processMessage(JSONObject jsonObject) {
        return concreteMessageFactory.createMessage(jsonObject);
    }

}
