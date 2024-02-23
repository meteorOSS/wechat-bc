package com.meteor.wechatbc.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleConfigurationSection implements ConfigurationSection {
    protected Map<String, Object> data = new LinkedHashMap<>();

    @Override
    public String getString(String path) {
        Object val = data.get(path);
        if (val instanceof String) {
            return (String) val;
        }
        return null;
    }

    @Override
    public int getInt(String path) {
        Object val = data.get(path);
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return 0;
    }

    @Override
    public boolean getBoolean(String path) {
        Object val = data.get(path);
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        return false;
    }

    @Override
    public double getDouble(String path) {
        Object val = data.get(path);
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }
        return 0.0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getList(String path) {
        Object val = data.get(path);
        if (val instanceof List) {
            return (List<?>) val;
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getStringList(String path) {
        Object val = data.get(path);
        if (val instanceof List) {
            return (List<String>) val;
        }
        return new ArrayList<>();
    }

    @Override
    public void set(String path, Object value) {
        data.put(path, value);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = data;

        for (String key : keys) {
            Object value = current.get(key);
            if (value instanceof Map) {
                current = (Map<String, Object>) value;
            } else {
                return null;
            }
        }

        SimpleConfigurationSection section = new SimpleConfigurationSection();
        section.data = current;
        return section;
    }

    @Override
    public boolean contains(String path) {
        return data.containsKey(path);
    }
}
