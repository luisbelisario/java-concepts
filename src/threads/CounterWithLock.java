package threads;
/**
 * Same workload as CounterWithoutLock, but using a thread-safe Counter.
 *
 * <p>Expected value = 2,000,000 * 2 = 4,000,000. Unlike the unsynchronized
 * version, this result is stable across runs.
 */
public class CounterWithLock {

    public static void main(String[] args) throws Exception {
        CounterThreadSafe counter = new CounterThreadSafe();

        Runnable task = () -> {
            for (int i = 0; i < 2_000_000; i++) {
                counter.increment();
            }
        };

        Thread a = new Thread(task, "A");
        Thread b = new Thread(task, "B");

        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("final value: " + counter.value());
        System.out.println("expected:    4000000");
    }
}
