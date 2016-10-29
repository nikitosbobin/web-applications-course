package core;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getId());
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getId());
        });
        thread.run();
        thread.start();
    }
}



