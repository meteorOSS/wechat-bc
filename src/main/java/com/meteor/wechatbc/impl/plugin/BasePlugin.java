package com.meteor.wechatbc.impl.plugin;


import com.meteor.wechatbc.command.WeChatCommand;
import com.meteor.wechatbc.config.YamlConfiguration;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.plugin.Plugin;
import com.meteor.wechatbc.plugin.PluginDescription;
import com.meteor.wechatbc.scheduler.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * 插件的基类
 */
public abstract class BasePlugin implements Plugin {

    private boolean enable = false;
    private PluginDescription pluginDescription;
    private Logger logger;

    private WeChatClient weChatClient;

    private YamlConfiguration yamlConfiguration;

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
        File file = new File(weChatClient.getDataFolder(),String.format("plugins/%s",getPluginDescription().getName()));
        if(!file.exists()) file.mkdirs();
        return file;
    }

    /**
     * 获取插件resources下的文件
     * @param file
     * @return
     */
    public InputStream getResource(String file){
        return getClass().getClassLoader().getResourceAsStream(file);
    }

    /**
     * 获取指令
     * @param command
     * @return
     */
    public WeChatCommand getCommand(String command){
        return weChatClient.getCommandManager().getWeChatCommandMap().get(command);
    }


    /**
     * 保存默认配置
     */
    public void saveDefaultConfig(){
        try(
                InputStream resource = getResource("config.yml");
                FileOutputStream fileOutputStream = new FileOutputStream(new File(getDataFolder(), "config.yml"));
        ) {
            byte[] bytes = new byte[resource.available()];
            int length = 0;
            while ((length = resource.read(bytes))!=-1){
                fileOutputStream.write(bytes,0,length);
            }
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重载配置文件
     */
    public void reloadConfig(){
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"config.yml"));
    }

    /**
     * 获取配置文件
     */
    public YamlConfiguration getConfig(){
        if(yamlConfiguration==null){
            File file = new File(getDataFolder(), "config.yml");
            if(file.exists())
                yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }
        return yamlConfiguration;
    }

    public Scheduler getScheduler(){
        return getWeChatClient().getScheduler();
    }

}
