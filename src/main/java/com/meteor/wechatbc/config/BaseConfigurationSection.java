package com.meteor.wechatbc.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseConfigurationSection implements ConfigurationSection {
    protected Map<String, Object> data = new HashMap<>();

    @Override
    public String getString(String path) {
        Object value = data.get(path);
        return value instanceof String ? (String) value : null;
    }

    @Override
    public int getInt(String path) {
        Object value = data.get(path);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    @Override
    public boolean getBoolean(String path) {
        Object value = data.get(path);
        return value instanceof Boolean ? (Boolean) value : false;
    }

    @Override
    public double getDouble(String path) {
        Object value = data.get(path);
        return value instanceof Number ? ((Number) value).doubleValue() : 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getList(String path) {
        Object value = data.get(path);
        return value instanceof List ? (List<?>) value : null;
    }

    @Override
    public List<String> getStringList(String path) {
        Object value = data.get(path);
        return value instanceof List ? (List<String>) value : null;
    }

    @Override
    public void set(String path, Object value) {
        data.put(path, value);
    }

    @Override
    public boolean contains(String path) {
        return data.containsKey(path);
    }

}
