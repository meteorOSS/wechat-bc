package com.meteor.wechatbc.scheduler;

import com.meteor.wechatbc.plugin.Plugin;

/**
 * 调度任务类
 */
public interface Task {

    /**
     * 持有该任务的插件
     * @return
     */
    Plugin getPlugin();

    /**
     * 检查任务是否已取消
     */
    boolean isCancelTask();

    /**
     * 获取任务的唯一标识
     */
    long getTaskId();

    /**
     * 取消任务
     */
    void cancelTask();

}
