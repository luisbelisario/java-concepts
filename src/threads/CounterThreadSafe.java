package threads;
/**
 * Thread-safe counter.
 *
 * <p>{@code increment()} and {@code value()} are synchronized on the same
 * monitor (this), so only one thread at a time can execute the read-add-write
 * sequence. The result is deterministic: total increments, no lost updates.
 */
public class CounterThreadSafe {
    private int value = 0;

    public synchronized void increment() {
        value++;
    }

    public synchronized int value() {
        return value;
    }
}
