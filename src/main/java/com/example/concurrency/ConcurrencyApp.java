package com.example.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyApp {

    private static final String KEY = "key";

    public static void main(String[] args) {
        CacheManager<String, String> manager = new CacheManager<>();
        manager.addData(KEY, "Value");

        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(31);
            for (int i = 0; i < 30; i++) {
                var id = i;
                service.execute(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread().getName() + " " + manager.get(KEY) + " " + id);
                });
            }
            service.execute(() -> {
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                }
                manager.addData(KEY, "Aux");
                System.out.println(Thread.currentThread().getName() + " " + manager.get(KEY) + " " + 30);
            });
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

}
