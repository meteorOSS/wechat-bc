package com.meteor.wechatbc.impl.command;

import com.meteor.wechatbc.command.WeChatCommand;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指令管理
 */
public class CommandManager {

    // 存储所有注册的指令
    private Map<String, WeChatCommand> weChatCommandMap = new ConcurrentHashMap<>();

    @Getter private final Logger logger = LogManager.getLogger("CommandManager");

    public Map<String, WeChatCommand> getWeChatCommandMap() {
        return weChatCommandMap;
    }

    public void registerCommand(WeChatCommand weChatCommand){
        String mainCommand = weChatCommand.getMainCommand();

        if(weChatCommandMap.containsKey(mainCommand)){
            logger.warn("注册指令" + weChatCommand.getMainCommand()+"时发生了冲突");
            return;
        }
        logger.debug("注册了指令 {}",mainCommand);
        weChatCommandMap.put(mainCommand,weChatCommand);
    }

    @Data
    public class ExecutorCommand{

        private String command;

        public ExecutorCommand(String command){
            this.command = command;
        }

        // 取得指令执行的子参数
        // 例如对于 test ag1 ag2 得到[ag1,ag2]元素
        public String[] formatArgs(){
            return Arrays.stream(command.split(" "))
                    .skip(1)
                    .toArray(String[]::new);
        }

        public String getMainCommand(){
            System.out.println(command);
            return command.substring(0,command.indexOf(" "));
        }
    }

    public ExecutorCommand getExecutorCommand(String command){
        return new ExecutorCommand(command);
    }


    public String[] formatArgs(String command){
        return new ExecutorCommand(command).formatArgs();
    }
}
