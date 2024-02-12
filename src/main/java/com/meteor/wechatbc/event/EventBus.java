package com.meteor.wechatbc.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    @Data
    @AllArgsConstructor
    private static class EventListener{
        private final Object target;
        private final Method method;
    }

    private final Map<Class<?>, List<EventListener>> listeners = new ConcurrentHashMap<>();

    public void register(Object obj){
        Method[] methods = obj.getClass().getDeclaredMethods();
        // 获得所有被 @EventHandler 注解的方法
        for (Method method : methods) {
            if(method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1){
                Class<?> eventType = method.getParameterTypes()[0];
                listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(new EventListener(obj, method));
            }
        }
    }

    /**
     * 调用事件
     */
    public void post(Object obj){
        List<EventListener> eventListeners = listeners.get(obj.getClass());
        if(eventListeners!=null){
            for (EventListener eventListener : eventListeners) {
                try {
                    eventListener.getMethod().invoke(eventListener.getTarget(), obj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
