package jp.tokyo.leon.customization.concurrent.s.future;

/**
 * @author leon
 * @date 2024/2/14 01:21
 */
public class FutureTaskTest {
    public static void main(String[] args) throws InterruptedException {
        SFutureTask<String> futureTask = new SFutureTask<>(() -> {
            Thread.sleep(5000);
            return "hello world";
        });

        new Thread(futureTask).start();

        System.out.println(futureTask.get());
    }
}
