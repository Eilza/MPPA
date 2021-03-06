package mipt.eilza;

import java.util.concurrent.atomic.AtomicLong;

public class CounterAtomicLong implements Counter {
    private volatile AtomicLong value = new AtomicLong(0);
    @Override
    public void increment() {
        this.value.incrementAndGet();
    }

    @Override
    public long getValue() {
        return value.longValue();
    }

    public void setValue(long value) {
        this.value.set(value);
    }
}
