package com.meteor.wechatbc.impl.console;

import com.meteor.wechatbc.impl.WeChatClient;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

import java.io.File;

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
        if(command.startsWith("upload")){
            String userName = command.replace("upload ", "");
            weChatClient.getWeChatCore().getHttpAPI().sendVideo(userName,new File(System.getProperty("user.dir"),"video/test.mp4"));
        }
    }

    @Override
    protected void shutdown() {

    }
}
