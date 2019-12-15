import java.util.concurrent.SynchronousQueue;

public class PingPongQueue {
    public static void main(String[] args) {
        SynchronousQueue<Integer> q = new SynchronousQueue<Integer>();
        PingPongQueue pingPongQueue = new PingPongQueue();
        PingThreadQueue pingThreadQueue = new PingThreadQueue(q);
        PongThreadQueue pongThreadQueue = new PongThreadQueue(q);
        pingThreadQueue.start();
        pongThreadQueue.start();
    }
}

class PingThreadQueue extends Thread {
    private SynchronousQueue<Integer> q;

    public PingThreadQueue(SynchronousQueue<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("ping");
            try {
                q.put(1);
                q.put(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class PongThreadQueue extends Thread {
    private SynchronousQueue<Integer> q;

    public PongThreadQueue(SynchronousQueue<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        while (true) {
            try {
                q.take();
                System.out.println("pong");
                q.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

