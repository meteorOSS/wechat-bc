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
    public static class EventListener{
        private Object target;
        private Method method;
        private ClassLoader classLoader;
    }


    private final Map<Class<?>, List<EventListener>> listeners = new ConcurrentHashMap<>();

    /**
     * 注册监听器类
     * @param obj
     */
    public void register(Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        ClassLoader classLoader = obj.getClass().getClassLoader(); // 获取类加载器
        for (Method method : methods) {
            if(method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1){
                Class<?> eventType = method.getParameterTypes()[0];
                listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(new EventListener(obj, method, classLoader));
            }
        }
    }

    /**
     * 卸载监听器类
     * @param obj
     */
    public void unRegister(Object obj){
        listeners.values().forEach(listeners -> listeners.removeIf(listener -> listener.getTarget() == obj));
    }

    /**
     * 调用事件
     */
    public void post(Object event) {
        List<EventListener> eventListeners = listeners.get(event.getClass());
        if(eventListeners != null) {
            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                for (EventListener eventListener : eventListeners) {
                    ClassLoader listenerClassLoader = eventListener.getClassLoader();
                    Thread.currentThread().setContextClassLoader(listenerClassLoader); // 切换类加载器
                    Method method = eventListener.getMethod();
                    boolean accessible = method.isAccessible();
                    method.setAccessible(true); // 使私有方法可访问
                    method.invoke(eventListener.getTarget(), event);
                    method.setAccessible(accessible);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } finally {
                Thread.currentThread().setContextClassLoader(originalClassLoader); // 恢复原类加载器
            }
        }
    }
}
