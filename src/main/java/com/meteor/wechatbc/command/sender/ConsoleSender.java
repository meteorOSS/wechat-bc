package com.meteor.wechatbc.command.sender;

import com.meteor.wechatbc.impl.WeChatClient;

/**
 * 由控制台执行
 */
public class ConsoleSender implements CommandSender{

    private WeChatClient weChatClient;

    public ConsoleSender(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
    }

    @Override
    public void sendMessage(String message) {
        weChatClient.getCommandManager().getLogger().info(message);
    }
}
