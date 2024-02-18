package com.meteor.wechatbc.command;

import lombok.Data;

@Data
public class WeChatCommand {
    private String mainCommand;
    private CommandExecutor commandExecutor;
    public WeChatCommand(String mainCommand){
        this.mainCommand = mainCommand;
    }
}
