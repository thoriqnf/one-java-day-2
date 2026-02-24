package com.Blubca.finance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlueBcaConcurrencySystem {
    public static void main(String[] args) {
        // 1. Membuat Thread Pool dengan 3 "pekerja" tetap
        // Ini lebih efisien daripada membuat Thread baru untuk setiap tugas
        ExecutorService adminPool = Executors.newFixedThreadPool(3);
        System.out.println("=== BlueBCA Notification Center ===");
        System.out.println("Memulai pengiriman notifikasi massal...\n");
        // 2. Simulasi 5 tugas pengiriman notifikasi
        for (int i = 1; i <= 5; i++) {
            final int customerId = i;
            // Mengirim tugas ke dalam pool (menggunakan Lambda)
            adminPool.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("[" + threadName + "] Mengirim notifikasi ke Nasabah ID: " + customerId);

                try {
                    // Simulasi waktu proses pengiriman (1 detik)
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("[" + threadName + "] Selesai mengirim ke Nasabah ID: " + customerId);
            });
        }

        // 3. Menutup ExecutorService setelah semua tugas selesai dimasukkan
        adminPool.shutdown();

        try {
            // Menunggu maksimal 1 menit sampai semua tugas benar-benar selesai
            if (!adminPool.awaitTermination(60, TimeUnit.SECONDS)) {
                adminPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            adminPool.shutdownNow();
        }

        System.out.println("\nStatus: Semua notifikasi BlueBCA telah diproses.");
    }
}
