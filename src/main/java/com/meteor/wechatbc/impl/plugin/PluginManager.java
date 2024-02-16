package com.meteor.wechatbc.impl.plugin;

import com.meteor.wechatbc.Main;
import com.meteor.wechatbc.plugin.BasePlugin;
import com.meteor.wechatbc.plugin.PluginClassLoader;
import com.meteor.wechatbc.plugin.PluginDescription;
import com.meteor.wechatbc.plugin.PluginLoader;

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


    public PluginManager(){
        // 创建plugins目录存放插件
        File pluginsFolder = new File(System.getProperty("user.dir"),"plugins");
        if(!pluginsFolder.exists()){
            pluginsFolder.mkdirs();
        }

        for (File pluginFile : pluginsFolder.listFiles()) {
            loadPlugin(pluginFile);
        }

        PluginLoader.logger.info("载入了 {} 个插件",pluginMap.size());

    }

    public void loadPlugin(File file){

        PluginLoader pluginLoader = new PluginLoader(file);

        PluginDescription pluginDescription = pluginLoader.getPluginDescription();

        if(pluginMap.containsKey(pluginDescription.getName())){
            PluginLoader.logger.info("插件 [{}] 已存在，无法重新加载",pluginDescription.getName());
            return;
        }

        URL[] urls = new URL[0];
        try {
            urls = new URL[]{ file.toURI().toURL() };
            PluginClassLoader pluginClassLoader = new PluginClassLoader(urls, Main.class.getClassLoader());

            // 加载插件主类
            Class<?> mainClass = Class.forName(pluginDescription.getMain(), true, pluginClassLoader);

            BasePlugin plugin = (BasePlugin) mainClass.getDeclaredConstructor().newInstance();

            if (!BasePlugin.class.isAssignableFrom(mainClass)) {
                throw new IllegalArgumentException("未寻找到插件主类:" + pluginDescription.getMain());
            }

            pluginMap.put(pluginDescription.getName(),plugin);

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
