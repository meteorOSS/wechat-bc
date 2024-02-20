package com.meteor.wechatbc.scheduler;

import com.meteor.wechatbc.impl.plugin.BasePlugin;

public abstract class WeChatRunnable implements Runnable{
    private Task task;

    public Task runTaskLater(BasePlugin plugin, long delay){
        this.check();
        task = plugin.getScheduler().runTaskLater(plugin,this,delay);
        return task;
    }

    public Task runTaskTimer(BasePlugin plugin,long delay,long period){
        this.check();
        task = plugin.getScheduler().runTaskTimer(plugin,this,delay,period);
        return task;
    }

    public Task runTask(BasePlugin plugin){
        this.check();
        task = plugin.getScheduler().runTask(plugin,this);
        return task;
    }

    private void check(){
        if(task!=null){
            throw new IllegalStateException("This runnable has already scheduled");
        }
    }
}
