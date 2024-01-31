package com.meteor.wechatbc.impl.console;

import com.meteor.wechatbc.impl.WeChatClient;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

public class Console extends SimpleTerminalConsole {

    private final WeChatClient weChatClient;

    public Console(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
    }


    @Override
    protected boolean isRunning() {
        return false;
    }

    @Override
    protected void runCommand(String command) {

    }

    @Override
    protected void shutdown() {

    }
}
