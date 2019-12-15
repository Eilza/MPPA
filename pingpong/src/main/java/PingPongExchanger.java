import java.util.concurrent.Exchanger;

public class PingPongExchanger {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<String>();
        Ping ping = new Ping(exchanger);
        Pong pong = new Pong(exchanger);
        ping.start();
        pong.start();
    }
}

class Pong extends Thread {
    private Exchanger<String> exchanger;

    Pong(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            while(true) {
                exchanger.exchange(null);
                exchanger.exchange(null);
                System.out.println("pong");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Ping extends Thread {
    private Exchanger<String> exchanger;

    Ping(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            while(true) {
                exchanger.exchange(null);
                System.out.println("ping");
                exchanger.exchange(null);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
