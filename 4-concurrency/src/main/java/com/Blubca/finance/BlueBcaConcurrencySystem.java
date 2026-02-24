package com.Blubca.finance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlueBcaConcurrencySystem {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== BlueBCA Notification Center ===\n");

        // ====================================================
        // STEP 1: Cara Lama — Membuat Thread manual untuk setiap tugas
        // Masalah: Kalau 1000 tugas, kamu buat 1000 Thread = boros memori!
        // ====================================================
        System.out.println("--- Cara Lama (Thread Manual) ---");
        for (int i = 1; i <= 2; i++) {
            final int id = i;
            Thread t = new Thread(() -> {
                System.out.println("[" + Thread.currentThread().getName() + "] Kirim ke Nasabah " + id);
            });
            t.start();
            t.join(); // Tunggu selesai sebelum lanjut
        }

        Thread.sleep(500); // Jeda agar output tidak tercampur
        System.out.println();

        // ====================================================
        // STEP 2: Cara Modern — ExecutorService (Thread Pool)
        // Bayangkan: 3 teller bank melayani 5 nasabah bergantian.
        // Teller tidak dipecat setelah 1 nasabah — mereka melayani nasabah berikutnya!
        // ====================================================
        System.out.println("--- Cara Modern (Thread Pool: 3 Pekerja) ---");
        ExecutorService adminPool = Executors.newFixedThreadPool(3);

        // STEP 3: Submit 5 tugas ke pool — hanya 3 yang jalan bersamaan
        for (int i = 1; i <= 5; i++) {
            final int customerId = i;
            // Lambda sebagai Runnable — tugas yang akan dijalankan oleh pool
            adminPool.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("[" + threadName + "] Mengirim notifikasi ke Nasabah ID: " + customerId);

                try {
                    // Simulasi waktu proses (1 detik per notifikasi)
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("[" + threadName + "] Selesai mengirim ke Nasabah ID: " + customerId);
            });
        }

        // STEP 4: Tutup pool — tidak menerima tugas baru
        adminPool.shutdown();

        // STEP 5: Tunggu sampai semua tugas selesai (maks 60 detik)
        // Jika timeout, paksa hentikan
        if (!adminPool.awaitTermination(60, TimeUnit.SECONDS)) {
            adminPool.shutdownNow();
        }

        System.out.println("\nStatus: Semua notifikasi BlueBCA telah diproses.");
    }
}
