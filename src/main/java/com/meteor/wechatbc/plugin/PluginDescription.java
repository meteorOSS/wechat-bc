package com.meteor.wechatbc.plugin;

import lombok.Data;

import java.util.List;

/**
 * 如bukkit插件中的plugin.yml一样描述一个插件
 */
@Data
public class PluginDescription {
    private String name;
    private String version;
    private List<String> authors;
    private String description;
    private String mainClassPath;
    private List<String> depend;
}
