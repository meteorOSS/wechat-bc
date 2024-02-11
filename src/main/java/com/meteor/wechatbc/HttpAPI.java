package com.meteor.wechatbc;

import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckResponse;

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
    SyncCheckResponse syncCheck();

    /**
     * 获取最新消息
     */
    JSONObject getMessage();

}
