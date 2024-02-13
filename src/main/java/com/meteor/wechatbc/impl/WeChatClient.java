package com.meteor.wechatbc.impl;

import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.console.Console;
import com.meteor.wechatbc.impl.contact.ContactManager;
import com.meteor.wechatbc.impl.event.EventManager;
import com.meteor.wechatbc.impl.plugin.PluginManager;
import com.meteor.wechatbc.impl.synccheck.SyncCheckRunnable;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * 客户端
 */
public class WeChatClient {
    @Getter private Logger logger;

    @Getter @Setter private WeChatCoreImpl weChatCore;

    private SyncCheckRunnable syncCheckRunnable;
    @Getter private EventManager eventManager;

    @Getter private ContactManager contactManager;

    @Getter private PluginManager pluginManager;

    public WeChatClient(Logger logger){
        this.logger = logger;
    }

    public void initWeChatCore(BaseRequest baseRequest) {
        this.weChatCore = new WeChatCoreImpl(this,
                baseRequest);
        this.weChatCore.getHttpAPI().init();
    }

    /**
     * 启动
     */
    public void start(){
        this.weChatCore.getHttpAPI().initWeChat();
        this.syncCheckRunnable = new SyncCheckRunnable(this);
        this.contactManager = new ContactManager(this);
        this.eventManager = new EventManager();
    }

    public void initPluginManager(){
        this.pluginManager = new PluginManager();
    }

    /**
     * 如果需要控制台的话
     * 调用该方法挂起
     */
    public void loop(){
        getLogger().info("启动控制台...");
        weChatCore.getHttpAPI().syncCheck();
        try {
            new Console(this).start();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("因为一些错误，控制台停止运行了");
        }
    }

    public void stop(){

    }

}
