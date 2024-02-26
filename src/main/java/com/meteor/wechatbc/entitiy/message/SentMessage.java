package com.meteor.wechatbc.entitiy.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.meteor.wechatbc.entitiy.SendMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentMessage {
    @JSONField(name = "Msg")
    private SendMessage sendMessage;
    private String msgId;
}
