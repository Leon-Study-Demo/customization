package jp.tokyo.leon.customization.concurrent.s.future;

import java.util.concurrent.Callable;

/**
 * @author leon
 * @date 2024/2/14 01:12
 */
public class SFutureTask<T> implements Runnable{

    private final Object lock = new Object();

    private T result;

    private final Callable<T> callable;

    public SFutureTask(Callable<T> callable) {
        this.callable = callable;
    }


    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    public T get() throws InterruptedException {
        synchronized (lock) {
            while (result == null) {
                lock.wait();
            }
            return result;
        }
    }
}
