package com.meteor.wechatbc.util.mode;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class GitHubRelease {
    @JSONField(name = "tag_name")
    private String tagName;
    private String name;
    private String body;
}