package com.meteor.wechatbc.config;

import java.util.List;

public interface ConfigurationSection {
    String getString(String path);
    int getInt(String path);
    boolean getBoolean(String path);
    double getDouble(String path);
    List<?> getList(String path);
    List<String> getStringList(String path);
    ConfigurationSection getConfigurationSection(String path);
    void set(String path, Object value);
    boolean contains(String path);
}
