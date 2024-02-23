package com.meteor.wechatbc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public abstract class FileConfiguration implements ConfigurationSection {
    protected Map<String, Object> data = new LinkedHashMap<>();

    public abstract void save(File file) throws IOException;
    public abstract void load(File file) throws IOException;

    @Override
    public String getString(String path) {
        Object value = data.get(path);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    @Override
    public int getInt(String path) {
        Object value = data.get(path);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
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
