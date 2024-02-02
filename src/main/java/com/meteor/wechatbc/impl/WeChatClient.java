package com.meteor.wechatbc.impl;

import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.console.Console;
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
    }

    /**
     * 如果需要控制台的话
     * 调用该方法挂起
     */
    public void loop(){
        getLogger().info("启动控制台...");
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
