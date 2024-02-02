package com.meteor.wechatbc.entitiy.session;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class Skey {

    @JSONField(name = "Key")
    private int Key;
    @JSONField(name = "Val")
    private long val;

}
