package com.meteor.wechatbc.impl;

import com.meteor.wechatbc.impl.model.storage.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 * 客户端
 */
public class WeChatClient {
    @Getter private Logger logger;

    @Getter @Setter private BaseRequest baseRequest;

    public WeChatClient(Logger logger){
        this.logger = logger;
    }

    /**
     * 如果需要控制台的话
     * 调用该方法挂起
     */
    public void loop(){

    }

}
