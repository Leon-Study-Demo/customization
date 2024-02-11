package jp.tokyo.leon.customization.concurrent.s.thread.pool;

/**
 * @author leon
 * @date 2024/2/11 13:36
 */
@FunctionalInterface
public interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}
