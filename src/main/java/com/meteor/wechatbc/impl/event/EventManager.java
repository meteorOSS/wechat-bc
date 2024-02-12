package com.meteor.wechatbc.impl.event;

import com.meteor.wechatbc.event.Event;
import com.meteor.wechatbc.event.EventBus;
import com.meteor.wechatbc.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件管理器
 */
public class EventManager {

    private final EventBus eventBus;

    private final Map<Plugin, List<Listener>> pluginListeners = new ConcurrentHashMap<>();

    public EventManager(){
        this.eventBus = new EventBus();
    }

    /**
     * 注册插件监听器
     * @param plugin
     * @param listener
     */
    public void registerPluginListener(Plugin plugin,Listener listener){
        eventBus.register(listener);
        pluginListeners.putIfAbsent(plugin,new ArrayList<>());
        pluginListeners.get(plugin).add(listener);
    }

    /**
     * 取消插件所有监听器
     * @param plugin
     */
    public void unRegisterPluginListener(Plugin plugin){
        List<Listener> listeners = pluginListeners.get(plugin);
        if(listeners!=null) {
            listeners.forEach(this::unRegisterListener);
        }
        pluginListeners.remove(plugin);
    }

    private void unRegisterListener(Listener listener){
        eventBus.unRegister(listener);
    }

    /**
     * 呼叫事件
     */
    public void callEvent(Event event){
        eventBus.post(event);
    }
}
