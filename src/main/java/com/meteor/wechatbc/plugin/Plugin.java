package com.meteor.wechatbc.plugin;


import org.apache.logging.log4j.Logger;

/**
 * 描述一个wechatbc插件
 */
public interface Plugin {

    Logger getLogger();

    void setEnable(boolean enable);

    // 是否启用插件
    boolean isEnable();

    void onLoad();

    void onEnable();

    void onDisable();
}
