package com.meteor.wechatbc.impl;

import com.meteor.wechatbc.entitiy.session.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

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
     * 如果需要控制台的话
     * 调用该方法挂起
     */
    public void loop(){

    }

}
