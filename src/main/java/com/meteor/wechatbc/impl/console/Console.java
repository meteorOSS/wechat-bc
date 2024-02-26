package com.meteor.wechatbc.impl.console;

import com.meteor.wechatbc.command.WeChatCommand;
import com.meteor.wechatbc.command.sender.ConsoleSender;
import com.meteor.wechatbc.entitiy.message.SentMessage;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.command.CommandManager;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

/**
 * 控制台
 */
public class Console extends SimpleTerminalConsole {

    private final WeChatClient weChatClient;

    public Console(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
        this.consoleSender = new ConsoleSender(weChatClient);
    }

    private final ConsoleSender consoleSender; // 控制台指令执行者

    @Override
    protected boolean isRunning() {
        return true;
    }


    @Override
    protected void runCommand(String command) {

        if(command.startsWith("test ")){
            SentMessage sentMessage = weChatClient.getWeChatCore().getHttpAPI().sendMessage(command.replace("test ", ""), "你好");
            weChatClient.getScheduler().runTaskLater(weChatClient.getEventManager().getDefaultPlugin(), ()->{
                weChatClient.getWeChatCore().getHttpAPI().revoke(sentMessage);
            },5);
        }

        CommandManager commandManager = weChatClient.getCommandManager();
        CommandManager.ExecutorCommand executorCommand = commandManager.getExecutorCommand(command);
        Optional.ofNullable(commandManager.getWeChatCommandMap().get(executorCommand.getMainCommand())).ifPresent(weChatCommand -> {
            weChatCommand.getCommandExecutor().onCommand(consoleSender, executorCommand.formatArgs());
        });
    }

    @Override
    protected void shutdown() {

    }
}
