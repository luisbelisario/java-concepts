package threads;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadStates {

    /**
     * Thread states and the intrinsic monitor.
     *
     * States (getState() returns a snapshot):
     *
     *   NEW — created, start() not called yet.
     *   RUNNABLE — running or ready to run; may also cover native I/O waits.
     *   BLOCKED — waiting to enter a synchronized block held by another thread.
     *   WAITING — waiting for a signal or for another thread to finish:
     *       wait(), join() without timeout.
     *   TIMED_WAITING — same as WAITING, but with a deadline:
     *       sleep(), wait(timeout), join(timeout).
     *   TERMINATED — run() finished.
     *
     *
     * BLOCKED vs WAITING: BLOCKED is the monitor entry queue — the thread wants
     * the lock and never released anything. WAITING is a deliberate wait: wait()
     * releases the monitor and puts the thread in the monitor wait set; join()
     * simply waits for another thread to terminate. After notify(), a thread in
     * wait() moves back to the entry queue and must acquire the lock again, so it
     * briefly becomes BLOCKED.
     *
     * A thread can own the monitor and still be TIMED_WAITING (e.g., sleeping
     * inside synchronized). getState() does not tell you who owns the lock.
     *
     * Monitor: every Java object has an intrinsic lock called monitor, used by
     * synchronized. An instance synchronized method uses the monitor of {@code this};
     * a static synchronized method uses the monitor of the Class object;
     * synchronized (obj) uses the monitor of obj. Only one thread owns a monitor
     * at a time. The monitor has no fairness, timeout or tryLock; for those, use
     * ReentrantLock. Prefer a private dedicated lock object over {@code this} when
     * the object is exposed, to avoid external code competing for the same monitor.
     */

    public static void main(String[] args) throws Exception {
        demoBlocked();
        demoWaiting();
        demoTimedWaiting();
    }

    static void demoBlocked() throws Exception {
        System.out.println("=== BLOCKED ===");
        Object lock = new Object();

        Thread first = new Thread(() -> {
            synchronized (lock) {
                sleep(2000);
            }
        }, "lock-owner");

        Thread second = new Thread(() -> {
            synchronized (lock) {
                System.out.println("  [second-lock] able to get in");
            }
        }, "second-lock");

        first.start();
        Thread.sleep(200);
        second.start();
        Thread.sleep(300);

        System.out.println("  first:      " + first.getState());
        System.out.println("  second: " + second.getState());

        first.join();
        second.join();
        System.out.println("  end: owner=" + first.getState()
                + ", second=" + second.getState());
        System.out.println();
    }


    static void demoWaiting() throws Exception {
        System.out.println("=== WAITING ===");
        Object monitor = new Object();
        AtomicBoolean ready = new AtomicBoolean(false);
        CountDownLatch enterInWait = new CountDownLatch(1);

        Thread waiting = getWaitingThread(monitor, enterInWait, ready);
        enterInWait.await();
        Thread.sleep(200);

        System.out.println("  waiting: " + waiting.getState());

        synchronized (monitor) {
            ready.set(true);
            monitor.notifyAll();       // acorda
        }

        waiting.join();
        System.out.println("  end: " + waiting.getState());
        System.out.println();
    }

    private static Thread getWaitingThread(Object monitor, CountDownLatch enterInWait, AtomicBoolean ready) {
        Thread waiting = new Thread(() -> {
            synchronized (monitor) {
                enterInWait.countDown();
                while (!ready.get()) {
                    try {
                        monitor.wait();        // WAITING
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            System.out.println("  [waiting-notify] woke up and out");
        }, "waiting-notify");

        waiting.start();
        return waiting;
    }


    static void demoTimedWaiting() throws Exception {
        System.out.println("=== TIMED_WAITING ===");
        Thread sleeping = new Thread(() -> sleep(2000), "sleeping");

        sleeping.start();
        Thread.sleep(200);
        System.out.println("  sleeping: " + sleeping.getState());

        sleeping.join();
        System.out.println("  end: " + sleeping.getState());
        System.out.println();
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
