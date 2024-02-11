package jp.tokyo.leon.customization.concurrent.s.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author leon
 * @date 2024/2/11 11:15
 */
@Slf4j
public class ThreadPool {
    // 阻塞队列
    private final BlockingQueue<Runnable> taskQueue;
    
    // 线程（worker）容器
    private final Set<Worker> workerSet = new HashSet<>();
    
    // 核心线程数
    private final int coreSize;

    // 超时时间
    private final long timeout;
    
    // 时间单位
    private final TimeUnit timeUnit;

    // 拒绝策略
    private final RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, int queueSize, long timeout, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue(queueSize);
        this.rejectPolicy = rejectPolicy;

    }
    
    public void execute(Runnable task) {
        synchronized (workerSet) {
            // 判断核心线程是否创建完成
            if (workerSet.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{}", worker);
                workerSet.add(worker);
                worker.start();
            } else {
                // taskQueue.put(task);（死等）
                // 队列满了如何操作？
                // 1）死等
                // 2）带超时时间的等待
                // 3）放弃任务执行
                // 4）抛出异常
                // 5）调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
        
    }

    // 线程对象封装成worker对象
    class Worker extends Thread {
        
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (task != null || (task = taskQueue.pull(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workerSet) {
                log.debug("worker 被移除{}", this);
                workerSet.remove(this);
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}
