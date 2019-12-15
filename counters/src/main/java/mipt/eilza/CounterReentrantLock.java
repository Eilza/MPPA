package mipt.eilza;

import java.util.concurrent.locks.ReentrantLock;

public class CounterReentrantLock implements Counter {
    ReentrantLock reentrantLock = new ReentrantLock();
    private volatile long value;
    @Override
    public void increment() {
        reentrantLock.lock();
        value++;
        reentrantLock.unlock();
    }

    @Override
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
