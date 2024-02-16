package com.meteor.wechatbc.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {
    private ClassLoader parentClassLoader;

    public PluginClassLoader(URL[] urls, ClassLoader parentClassLoader) {
        super(urls, parentClassLoader);
        this.parentClassLoader = parentClassLoader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // 首先尝试插件类加载器加载类
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            // 如果失败，尝试主应用类加载器加载类
            return parentClassLoader.loadClass(name);
        }
    }
}
