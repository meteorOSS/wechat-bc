package com.meteor.wechatbc.impl.plugin;

import com.meteor.wechatbc.Main;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.plugin.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件管理器
 */
public class PluginManager {

    private Map<String,BasePlugin> pluginMap = new ConcurrentHashMap<>();


    private WeChatClient weChatClient;

    public PluginManager(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
        // 创建plugins目录存放插件
        File pluginsFolder = new File(System.getProperty("user.dir"),"plugins");
        if(!pluginsFolder.exists()){
            pluginsFolder.mkdirs();
        }
        for (File pluginFile : pluginsFolder.listFiles()) {
            this.loadPlugin(pluginFile);
        }
        PluginLoader.logger.info("载入了 {} 个插件",pluginMap.size());
    }

    /**
     * 卸载插件
     */
    public void unload(BasePlugin plugin){
        String pluginName = plugin.getPluginDescription().getName();
        weChatClient.getEventManager().unRegisterPluginListener(plugin);
        pluginMap.remove(plugin,pluginName);
        PluginLoader.logger.info("已卸载 {}",pluginName);
    }

    /**
     * 加载插件
     * @param file
     */
    public void loadPlugin(File file){
        PluginLoader pluginLoader = new PluginLoader(file);
        // 获取插件描述信息
        PluginDescription pluginDescription = pluginLoader.getPluginDescription();
        // 如果插件已加载，则终止后面的逻辑
        if(pluginMap.containsKey(pluginDescription.getName())){
            PluginLoader.logger.info("插件 [{}] 已存在，无法重新加载",pluginDescription.getName());
            return;
        }
        URL[] urls = new URL[0];
        try {
            urls = new URL[]{ file.toURI().toURL() };
            // 为每个插件单独开一个类加载器以隔离
            PluginClassLoader pluginClassLoader = new PluginClassLoader(urls, Main.class.getClassLoader());
            // 加载插件主类
            Class<?> mainClass = Class.forName(pluginDescription.getMain(), true, pluginClassLoader);
            // 实例化插件主类
            BasePlugin plugin = (BasePlugin) mainClass.getDeclaredConstructor().newInstance();
            // 如果主类不是BasePlugin的子类
            if (!BasePlugin.class.isAssignableFrom(mainClass)) {
                throw new IllegalArgumentException("加载插件时发生了一个错误,主类必须继承自 BasePlugin " + pluginDescription.getMain());
            }
            pluginMap.put(pluginDescription.getName(),plugin);
            // 初始化插件
            plugin.init(pluginDescription,weChatClient);
            // 调用插件启动hook
            plugin.onEnable();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
