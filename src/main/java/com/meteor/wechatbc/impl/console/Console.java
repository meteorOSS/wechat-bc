package com.meteor.wechatbc.impl.console;

import com.meteor.wechatbc.impl.WeChatClient;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

/**
 * 控制台
 */
public class Console extends SimpleTerminalConsole {

    private final WeChatClient weChatClient;

    public Console(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
    }


    @Override
    protected boolean isRunning() {
        return true;
    }

    @Override
    protected void runCommand(String command) {
        System.out.println("运行指令");
    }

    @Override
    protected void shutdown() {

    }
}
