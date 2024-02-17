package com.meteor.wechatbc.command;

import com.meteor.wechatbc.command.sender.CommandSender;

/**
 * 指令执行的具体委托类接口
 */
public interface CommandExecutor {

    void onCommand(CommandSender commandSender,String[] args);

}
