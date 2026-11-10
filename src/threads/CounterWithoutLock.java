package threads;
public class CounterWithoutLock {

/**
 * Race condition demo: shared counter without synchronization.
 *
 * {@code value++} is not atomic: it reads, adds, and writes. Two threads
 * sharing the same static field can interleave those steps:
 *
 *   A reads 10
 *   B reads 10
 *   A writes 11
 *   B writes 11   // A's increment is lost
 *
 * This is a lost update. The result is never higher than the total number
 * of increments, usually lower, and changes on every run. In a tight loop it
 * tends to lose about half of them.
 *
 * Expected value = iterations per thread * number of threads.
 * With 2 threads and 2,000,000 each, the correct result is 4,000,000.
 * Instead, with the current code, we receive always a smaller value
 *
 */

    static int value = 0;

    public static void main(String[] args) throws Exception {

        Runnable task = () -> {
            for (int i = 0; i < 2_000_000; i++) {
                value++;   // read, add, write
            }
        };

        Thread a = new Thread(task, "A");
        Thread b = new Thread(task, "B");

        a.start(); // asks the JVM to create the thread and execute run().
        b.start();
        a.join();
        // it causes the calling thread (is this case the main)
        // to wait for the target thread to finish.
        // this is not exclusive to the main thread;
        // any thread can call join() on another.
        b.join();

        System.out.println("final value: " + value);
        System.out.println("expected:    4000000");
    }
}
