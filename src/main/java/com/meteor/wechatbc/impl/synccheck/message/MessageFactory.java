package com.meteor.wechatbc.impl.synccheck.message;

import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.entitiy.message.Message;

public interface MessageFactory {
    Message createMessage(JSONObject message);
}
