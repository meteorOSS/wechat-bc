package com.meteor.wechatbc.entitiy;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SendMessage {
    @JSONField(name = "Type")
    private String type;
    @JSONField(name = "Content")
    private String content = "";
    @JSONField(name = "FromUserName")
    private String fromUserName;
    @JSONField(name = "ToUserName")
    private String toUserName;
    @JSONField(name = "LocalID")
    private String localId;
    @JSONField(name = "ClientMsgId")
    private String clientMsgId;
    @JSONField(name = "MediaId")
    private String mediaId = "";
}
