package com.meteor.wechatbc.impl.event.sub;

import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 所有消息事件的基类
 */
@AllArgsConstructor
public class MessageEvent extends Event {
    @Getter private Message message;

    public String getContent(){
        return message.getContent();
    }


}
