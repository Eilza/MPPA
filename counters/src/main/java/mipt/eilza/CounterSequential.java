package mipt.eilza;

/*
Обычный однопоточный каунтер
 */
public class CounterSequential implements Counter{
    private long value;
    @Override
    public void increment() {
        value++;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public void setValue(long i) {
        this.value = value;
    }
}
