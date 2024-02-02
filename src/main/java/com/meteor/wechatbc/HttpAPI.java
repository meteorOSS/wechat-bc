package com.meteor.wechatbc;

import com.meteor.wechatbc.entitiy.SyncCheckSelector;

public interface HttpAPI {

    /**
     * 初始化
     */
    void init();

    /**
     * 初始化微信接口
     */
    void initWeChat();

    /**
     * 检查新消息
     */
    SyncCheckSelector syncCheck();

}
