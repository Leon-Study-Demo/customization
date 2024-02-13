package jp.tokyo.leon.customization.concurrent.s.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author leon
 * @date 2024/2/11 11:43
 */
@Slf4j
public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1, 1000, TimeUnit.MILLISECONDS,(queue, task) -> {
            // 1）死等
            // queue.put(task);

            // 2）带超时时间的等待
            // queue.offer(task, 1500, TimeUnit.MILLISECONDS);

            // 3）放弃任务执行
            // log.debug("放弃{}", task);

            // 4）抛出异常
            // throw new RuntimeException("任务执行失败" + task);
            // 5）调用者自己执行任务
            task.run();



        });
        for (int i = 0; i < 5; i++) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("正在执行的任务比编号{}", j);
            });
        }
    }
}
