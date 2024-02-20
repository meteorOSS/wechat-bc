package com.meteor.wechatbc.scheduler;

import com.meteor.wechatbc.plugin.Plugin;

/**
 * 任务调度器
 */
public interface Scheduler {


    /**
     * 在指定的延迟后执行一次性任务。
     *
     * @param plugin 插件实例
     * @param task 要执行的任务
     * @param delay 延迟（秒）
     * @return 调度的任务
     */
    Task runTaskLater(Plugin plugin, Runnable task,long delay);

    /**
     * 以固定的周期执行任务。
     *
     * @param plugin 插件实例
     * @param task 要执行的任务
     * @param delay 初始延迟（以服务器的tick为单位）
     * @param period 执行周期（以服务器的tick为单位）
     * @return 调度的任务
     */
    Task runTaskTimer(Plugin plugin, Runnable task, long delay, long period);


    /**
     * 立即执行一个任务。
     *
     * @param plugin 插件实例
     * @param task 要执行的任务
     * @return 调度的任务
     */
    Task runTask(Plugin plugin, Runnable task);

}
