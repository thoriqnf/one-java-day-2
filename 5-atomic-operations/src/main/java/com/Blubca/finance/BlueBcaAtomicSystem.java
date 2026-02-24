package com.Blubca.finance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class BlueBcaAtomicSystem {
    // Menggunakan AtomicInteger untuk menjamin keamanan urutan angka
    private static final AtomicInteger queueCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== BlueBCA Digital Queue System ===");

        // Membuat pool untuk mensimulasikan 10 nasabah yang ambil antrean bersamaan
        ExecutorService branchPool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            branchPool.submit(() -> {
                // incrementAndGet() adalah operasi atomic: tambah 1 dan ambil hasilnya
                int myNumber = queueCounter.incrementAndGet();
                System.out.println(Thread.currentThread().getName() +
                        " -> Nasabah berhasil ambil antrean nomor: " + myNumber);
            });
        }

        branchPool.shutdown();
        branchPool.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("\nTotal nasabah dalam antrean saat ini: " + queueCounter.get());
    }
}
