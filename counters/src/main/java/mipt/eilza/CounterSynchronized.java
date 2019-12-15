package mipt.eilza;

public class CounterSynchronized implements Counter {
    private volatile long value;
    @Override
    public synchronized void increment() {
        value++;
    }

    @Override
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
