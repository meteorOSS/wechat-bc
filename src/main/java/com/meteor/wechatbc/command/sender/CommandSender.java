package com.meteor.wechatbc.command.sender;

/**
 * 指令的执行者
 */
public interface CommandSender {
    void sendMessage(String message);
}
