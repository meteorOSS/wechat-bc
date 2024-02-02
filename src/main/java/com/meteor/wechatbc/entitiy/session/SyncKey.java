package com.meteor.wechatbc.entitiy.session;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 同步消息使用
 * 这里的值每次使用过后都会更新
 */
@Data
public class SyncKey {
    @JSONField(name = "Count")
    private int count;
    @JSONField(name = "List")
    private Skey[] keys;
}
