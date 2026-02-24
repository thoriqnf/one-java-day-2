package com.Blubca.finance;

import java.util.stream.LongStream;

public class BlueBcaParallelSystem {
    public static void main(String[] args) {
        System.out.println("=== BlueBCA Large Scale Data Audit ===");

        long start = System.currentTimeMillis();

        // Simulasi audit 10 juta ID transaksi
        long count = LongStream.rangeClosed(1, 10_000_000)
                .parallel() // Mengaktifkan pemrosesan paralel di semua core CPU
                .filter(id -> id % 1_000_000 == 0)
                .count();

        long end = System.currentTimeMillis();

        System.out.println("Audit selesai. Ditemukan " + count + " transaksi kritikal.");
        System.out.println("Waktu pemrosesan: " + (end - start) + " ms");
    }
}
