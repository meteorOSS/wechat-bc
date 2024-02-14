package com.meteor.wechatbc.plugin;


import com.meteor.wechatbc.Main;
import com.meteor.wechatbc.impl.WeChatClient;
import org.apache.logging.log4j.Logger;

/**
 * 插件的基类
 */
public abstract class BasePlugin implements Plugin {
    private boolean enable = false;
    private PluginDescription pluginDescription;
    private Logger logger;

    public BasePlugin(){

    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public WeChatClient getWeChatClient(){
        return Main.weChatClient;
    }
}
