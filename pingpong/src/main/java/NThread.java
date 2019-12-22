import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class NThread {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(false);
        TokenRing tokenRing = new TokenRing(lock);
        for (int i = 0; i < N_THREADS; ++i) {
            new ThreadPlayer(tokenRing, i).start();
        }
    }

    public static final int N_THREADS = 4;

    public static String getMessage(int index) {
        return index % 2 == 0 ? "ping" : "pong";
    }
}

class TokenRing {
    private Lock lock;
    private volatile String massage;
    private volatile int index = 0;

    public TokenRing(Lock lock) {
        this.lock = lock;
    }

    public void write(int i) {
        lock.lock();
        if(massage == null) {
            massage = "ping";
        }
        if (index == i) {
            massage = massage.equals("ping") ? "pong" : "ping";
            System.out.println(massage + " " + i);
            index = (i + 1) % NThread.N_THREADS;
        }
        lock.unlock();
    }
}


class ThreadPlayer extends Thread {
    TokenRing tokenRing;
    int i;

    public ThreadPlayer(TokenRing tokenRing, int i) {
        this.tokenRing = tokenRing;
        this.i = i;
    }

    @Override
    public void run() {
        while (true) {
            tokenRing.write(i);
        }
    }
}