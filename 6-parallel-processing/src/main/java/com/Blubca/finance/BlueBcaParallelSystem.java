package com.Blubca.finance;

import java.util.stream.LongStream;

public class BlueBcaParallelSystem {
    public static void main(String[] args) {

        System.out.println("=== BlueBCA Large Scale Data Audit ===\n");

        // ====================================================
        // ROUND 1: SEQUENTIAL — pakai 1 core CPU saja
        // Bayangkan: 1 kasir melayani 10 juta nasabah sendirian
        // ====================================================
        System.out.println("--- Round 1: Sequential (1 Core) ---");
        long startSeq = System.currentTimeMillis();

        long countSeq = LongStream.rangeClosed(1, 10_000_000)
                .filter(id -> id % 1_000_000 == 0)  // Ambil kelipatan 1 juta
                .count();

        long endSeq = System.currentTimeMillis();
        System.out.println("Ditemukan: " + countSeq + " transaksi kritikal");
        System.out.println("Waktu: " + (endSeq - startSeq) + " ms\n");

        // ====================================================
        // ROUND 2: PARALLEL — pakai SEMUA core CPU
        // Bayangkan: 4 kasir bagi tugas, masing-masing cek 2.5 juta data
        // ====================================================
        System.out.println("--- Round 2: Parallel (Semua Core) ---");
        long startPar = System.currentTimeMillis();

        long countPar = LongStream.rangeClosed(1, 10_000_000)
                .parallel()   // Satu kata ini = pakai semua core CPU!
                .filter(id -> id % 1_000_000 == 0)
                .count();

        long endPar = System.currentTimeMillis();
        System.out.println("Ditemukan: " + countPar + " transaksi kritikal");
        System.out.println("Waktu: " + (endPar - startPar) + " ms\n");

        // STEP 3: Perbandingan
        long seqTime = endSeq - startSeq;
        long parTime = endPar - startPar;
        System.out.println(">> Parallel " + (seqTime > parTime ? "lebih cepat" : "sama/lebih lambat")
                + "! (Sequential: " + seqTime + "ms vs Parallel: " + parTime + "ms)");
    }
}
