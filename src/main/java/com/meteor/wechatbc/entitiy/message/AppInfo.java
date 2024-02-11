package com.meteor.wechatbc.entitiy.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AppInfo {
    @JSONField(name = "AppID")
    private String appId;

    @JSONField(name = "Type")
    private Integer type;
}
