package com.meteor.wechatbc.entitiy.session;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Skey implements Serializable {

    @JSONField(name = "Key")
    private int Key;
    @JSONField(name = "Val")
    private long val;

}
