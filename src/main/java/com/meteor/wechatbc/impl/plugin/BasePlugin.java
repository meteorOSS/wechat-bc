package com.meteor.wechatbc.impl.plugin;


import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.plugin.Plugin;
import com.meteor.wechatbc.plugin.PluginDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;

/**
 * 插件的基类
 */
public abstract class BasePlugin implements Plugin {

    private boolean enable = false;
    private PluginDescription pluginDescription;
    private Logger logger;

    private WeChatClient weChatClient;

    /**
     * 初始化插件
     */
    public void init(
            PluginDescription pluginDescription,
            WeChatClient weChatClient
    ){
        this.pluginDescription = pluginDescription;
        this.weChatClient  = weChatClient;
        this.logger = LogManager.getLogger(pluginDescription.getName());
    }

    public BasePlugin(){
    }

    @Override
    public void onDisable() {
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public WeChatClient getWeChatClient(){
        return weChatClient;
    }

    public PluginDescription getPluginDescription() {
        return pluginDescription;
    }

    /**
     * 获取插件资源文件路径
     * @return
     */
    public File getDataFolder(){
        File file = new File(weChatClient.getDataFolder(),getPluginDescription().getName());
        if(!file.exists()) file.mkdirs();
        return file;
    }

    /**
     * 获取插件resources下的文件
     * @param file
     * @return
     */
    public InputStream getResource(String file){
        return getClass().getClassLoader().getResourceAsStream("resources/"+file);
    }

}
