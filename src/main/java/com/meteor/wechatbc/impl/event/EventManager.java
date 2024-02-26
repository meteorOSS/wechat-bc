package com.meteor.wechatbc.impl.event;

import com.meteor.wechatbc.event.Event;
import com.meteor.wechatbc.event.EventBus;
import com.meteor.wechatbc.impl.DefaultPlugin;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.event.listener.ContactCommandListener;
import com.meteor.wechatbc.plugin.Plugin;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件管理器
 */
public class EventManager {

    @Getter private WeChatClient weChatClient;

    private final EventBus eventBus;

    @Getter private final DefaultPlugin defaultPlugin = new DefaultPlugin();

    private final Map<Plugin, List<Listener>> pluginListeners = new ConcurrentHashMap<>();

    public EventManager(WeChatClient weChatClient){
        this.eventBus = new EventBus();
        this.weChatClient = weChatClient;
        this.registerDefaultListener();
    }

    /**
     * 载入一些预定义的监听器
     */
    public void registerDefaultListener(){
        registerPluginListener(defaultPlugin,new ContactCommandListener(weChatClient));
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
        event.setEventManager(this);
        eventBus.post(event);
    }
}
