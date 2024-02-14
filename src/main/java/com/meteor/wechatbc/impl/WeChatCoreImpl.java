package com.meteor.wechatbc.impl;

import com.meteor.wechatbc.HttpAPI;
import com.meteor.wechatbc.WeChatCore;
import com.meteor.wechatbc.entitiy.session.BaseRequest;
import com.meteor.wechatbc.impl.model.Session;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

public class WeChatCoreImpl implements WeChatCore {

    private WeChatClient weChatClient;
    @Getter private Session session;

    private HttpAPI httpAPI;


    public WeChatCoreImpl(WeChatClient weChatClient, BaseRequest baseRequest){
        this.weChatClient = weChatClient;
        this.session = new Session();
        this.session.setBaseRequest(baseRequest);
        this.httpAPI = new HttpAPIImpl(this.weChatClient);
    }

    @Override
    public HttpAPI getHttpAPI() {
        return httpAPI;
    }

    @Override
    public String getAPIVersion() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }
}
