package com.meteor.wechatbc.impl.scheduler;

import com.meteor.wechatbc.plugin.Plugin;
import com.meteor.wechatbc.scheduler.Scheduler;
import com.meteor.wechatbc.scheduler.Task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerImpl implements Scheduler {
    private final ScheduledExecutorService executor;
    private final AtomicLong taskIdCounter = new AtomicLong(0);

    public SchedulerImpl() {
        this.executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public Task runTask(Plugin plugin, Runnable task) {
        long taskId = taskIdCounter.incrementAndGet();
        executor.execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().error("任务执行异常", e);
            }
        });
        return new SimpleScheduledTask(plugin,taskId);
    }

    @Override
    public Task runTaskLater(Plugin plugin, Runnable task, long delay) {
        long taskId = taskIdCounter.incrementAndGet();
        ScheduledFuture<?> future = executor.schedule(() -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().error("延迟任务执行异常", e);
            }
        }, delay, TimeUnit.SECONDS);
        return new SimpleScheduledTask(plugin,taskId, future);
    }

    @Override
    public Task runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        long taskId = taskIdCounter.incrementAndGet();
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                plugin.getLogger().error("周期任务执行异常", e);
            }
        }, delay, period, TimeUnit.SECONDS);
        return new SimpleScheduledTask(plugin,taskId, future);
    }

    private static class SimpleScheduledTask implements Task {
        private final Plugin plugin;
        private final long taskId;
        private final ScheduledFuture<?> future;

        private SimpleScheduledTask(Plugin plugin,long taskId) {
            this.plugin = plugin;
            this.taskId = taskId;
            this.future = null;
        }

        private SimpleScheduledTask(Plugin plugin,long taskId, ScheduledFuture<?> future) {
            this.plugin = plugin;
            this.taskId = taskId;
            this.future = future;
        }


        @Override
        public void cancelTask() {
            if (future != null) {
                future.cancel(false);
            }
        }

        @Override
        public boolean isCancelTask() {
            return future != null && future.isCancelled();
        }

        @Override
        public Plugin getPlugin() {
            return plugin;
        }

        @Override
        public long getTaskId() {
            return taskId;
        }

    }
}