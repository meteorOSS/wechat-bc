package com.meteor.wechatbc.entitiy.session;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 同步消息使用
 * 这里的值每次使用过后都会更新
 */
@Data
public class SyncKey implements Serializable {
    @JSONField(name = "Count")
    private int count;
    @JSONField(name = "List")
    private Skey[] keys;

    /**
     * 在synccheck接口需要用到格式化后的字符串作为参数
     * @return
     */
    @Override
    public String toString(){
        return Arrays.stream(keys)
                .map(skey -> String.format("%s_%s",skey.getKey(),skey.getVal()))
                .collect(Collectors.joining("|"));
    }
}
