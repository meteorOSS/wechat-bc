package com.meteor.wechatbc.impl;

import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.command.CommandManager;
import com.meteor.wechatbc.impl.console.Console;
import com.meteor.wechatbc.impl.contact.ContactManager;
import com.meteor.wechatbc.impl.event.EventManager;
import com.meteor.wechatbc.impl.fileupload.FileChunkUploader;
import com.meteor.wechatbc.impl.plugin.PluginManager;
import com.meteor.wechatbc.impl.synccheck.SyncCheckRunnable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * 客户端
 */
@NoArgsConstructor
public class WeChatClient {
    @Getter private Logger logger;

    @Getter @Setter private WeChatCoreImpl weChatCore;

    private SyncCheckRunnable syncCheckRunnable;
    @Getter private EventManager eventManager;

    @Getter private ContactManager contactManager;

    @Getter private PluginManager pluginManager;

    @Getter private CommandManager commandManager;

    public WeChatClient(Logger logger){
        this.logger = logger;
    }

    public boolean initWeChatCore(BaseRequest baseRequest) {
        try {
            this.weChatCore = new WeChatCoreImpl(this,
                    baseRequest);
            this.weChatCore.getHttpAPI().init();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 一些必要的目录
     */
    public void mkDirs(){
        File dataFolder = getDataFolder();
        File iconFolder = new File(dataFolder,"img/icon");
        if(!iconFolder.exists()) iconFolder.mkdirs();
    }

    /**
     * 启动
     */
    public void start(){
        this.weChatCore.getHttpAPI().initWeChat();
        this.syncCheckRunnable = new SyncCheckRunnable(this);
        this.contactManager = new ContactManager(this);
        this.eventManager = new EventManager(this);
        this.commandManager = new CommandManager();

        // 初始化文件上传服务
        FileChunkUploader.init(this);
        this.mkDirs();

    }

    public void initPluginManager(){
        this.pluginManager = new PluginManager(this);
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

    public File getDataFolder(){
        return new File(System.getProperty("user.dir"));
    }

}
