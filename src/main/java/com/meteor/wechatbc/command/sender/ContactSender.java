package com.meteor.wechatbc.command.sender;

import com.meteor.wechatbc.impl.HttpAPI;
import com.meteor.wechatbc.Main;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.impl.WeChatClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 由联系人执行
 */
@AllArgsConstructor
public class ContactSender implements CommandSender{
    @Getter private Contact contact; // 指令的执行者

    private String formUserName; // 消息的发出地 (群聊，或私聊窗口等)

    // 这里的实现其实不是很优雅，有时间再改
    @Override
    public void sendMessage(String message) {
        WeChatClient weChatClient = Main.weChatClient;
        HttpAPI httpAPI = weChatClient.getWeChatCore().getHttpAPI();
        httpAPI.sendMessage(formUserName,message);
    }
}
