package com.Blubca.finance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class BlueBcaAtomicSystem {

    // Counter TIDAK aman — bisa kacau kalau diakses banyak thread bersamaan
    private static int unsafeCounter = 0;

    // Counter AMAN — AtomicInteger menjamin setiap operasi tidak terganggu thread lain
    private static final AtomicInteger safeCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== BlueBCA Digital Queue System ===\n");

        // ====================================================
        // ROUND 1: UNSAFE — pakai int biasa (bisa SALAH!)
        // Bayangkan: 2 orang pencet tombol antrean bersamaan.
        // Mesin baca nomor "5", dua-duanya dapat "6". Nomor dobel!
        // ====================================================
        System.out.println("--- Round 1: UNSAFE (int biasa) ---");
        ExecutorService unsafePool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            unsafePool.submit(() -> {
                unsafeCounter++; // BAHAYA: baca-tambah-tulis TIDAK atomic!
            });
        }

        unsafePool.shutdown();
        unsafePool.awaitTermination(1, TimeUnit.MINUTES);

        // Harusnya 1000, tapi sering KURANG karena race condition
        System.out.println("Hasil unsafe (harusnya 1000): " + unsafeCounter);
        System.out.println("Benar? " + (unsafeCounter == 1000 ? "YA" : "TIDAK — race condition!"));

        // ====================================================
        // ROUND 2: SAFE — pakai AtomicInteger (SELALU benar!)
        // incrementAndGet() = baca + tambah + tulis dalam SATU operasi
        // Tidak bisa diinterupsi oleh thread lain di tengah jalan
        // ====================================================
        System.out.println("\n--- Round 2: SAFE (AtomicInteger) ---");
        ExecutorService safePool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            safePool.submit(() -> {
                safeCounter.incrementAndGet(); // AMAN: operasi atomic!
            });
        }

        safePool.shutdown();
        safePool.awaitTermination(1, TimeUnit.MINUTES);

        // Selalu tepat 1000 — tidak ada data hilang
        System.out.println("Hasil safe (harusnya 1000):   " + safeCounter.get());
        System.out.println("Benar? " + (safeCounter.get() == 1000 ? "YA — selalu akurat!" : "TIDAK"));
    }
}
