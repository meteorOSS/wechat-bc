package com.meteor.wechatbc.impl.event.sub;

import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.entitiy.message.Message;

/**
 * 群聊消息
 */
public class GroupMessageEvent extends ReceiveMessageEvent{

    public GroupMessageEvent(Message message) {
        super(message);
    }

    /**
     * 获取发送消息的群聊
     * @return
     */
    public Contact getGroup(){
        Message message = getMessage();
        Contact contact = getEventManager().getWeChatClient().getContactManager().getContactCache().get(message.getFromUserName());
        return contact;
    }

}
