package Thread.lockobjecttest;

public class Semaphore {
    public static void main(String[] args) {
        SomeResource resource = new SomeResource(3);

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> resource.use());
            t.start();
        }
    }
}


class SomeResource {

    private final java.util.concurrent.Semaphore semaphore;

    SomeResource(int maxThread) {
        this.semaphore = new java.util.concurrent.Semaphore(maxThread);
    }

    public void use() {
        try {
            semaphore.acquire();
            System.out.println("[" + Thread.currentThread().getName() + "]"
                + semaphore.toString() + " 사용중");
            Thread.sleep((long) (Math.random() * 10000));
            System.out.println("[" + Thread.currentThread().getName() + "] 종료");
            // Thread 가 semaphore에게 종료를 알림
            semaphore.release();
        } catch (InterruptedException ignored) {}
    }
}