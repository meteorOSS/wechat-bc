package com.meteor.wechatbc.plugin;


import org.apache.logging.log4j.Logger;

/**
 * 描述一个wechatbc插件
 */
public interface Plugin {

    Logger getLogger();

    void onLoad();

    void onEnable();

    void onDisable();
}
