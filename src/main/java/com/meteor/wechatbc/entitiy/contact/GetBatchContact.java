package com.meteor.wechatbc.entitiy.contact;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GetBatchContact {

    @JSONField(name="UserName")
    private String userName;

    @JSONField(name="EncryChatRoomId")
    private String encryChatRoomId;

}
