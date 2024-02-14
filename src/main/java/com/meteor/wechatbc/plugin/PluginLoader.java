package com.meteor.wechatbc.plugin;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private final File file;

    public static Logger logger = LogManager.getLogger("plugin-loader");

    @Getter private PluginDescription pluginDescription;

    public PluginLoader(File file){
        this.file = file;
        this.loadPlugin();
    }
    /**
     * 获得BasePlugin实例
     */
    public void loadPlugin(){
        try (JarFile jarFile = new JarFile(file)) {
            this.pluginDescription = pluginDescription(jarFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得 PluginDescription
     */
    private PluginDescription pluginDescription(JarFile jarFile){
        JarEntry entry = jarFile.getJarEntry("plugin.yml");
        if (entry != null) {
            try (InputStream input = jarFile.getInputStream(entry)) {
                Yaml yaml = new Yaml();
                return yaml.loadAs(input, PluginDescription.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.info("尝试载入的插件不存在 plugin.yml!!");
        }
        return null;
    }

}
