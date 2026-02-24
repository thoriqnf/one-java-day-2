package com.Blubca.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class BlueBcaCashbackSystem {
    public static void main(String[] args) {
        // 1. Source: Daftar transaksi nasabah hari ini
        List<BigDecimal> transactions = Arrays.asList(
            new BigDecimal("50000"),      // Tidak dapat cashback
            new BigDecimal("200000"),     // Dapat
            new BigDecimal("1500000"),    // Dapat
            new BigDecimal("75000"),      // Tidak dapat
            new BigDecimal("300000")      // Dapat
        );

        System.out.println("=== BlueBCA Cashback Processor ===");
        // 2. Pipeline: Filtering -> Mapping -> Aggregation (Reduce)
        BigDecimal totalCashback = transactions.stream()
            // FILTERING: Ambil transaksi > 100.000
            .filter(amt -> amt.compareTo(new BigDecimal("100000")) > 0)

            // MAPPING: Hitung cashback 5% untuk tiap transaksi yang lolos filter
            .map(amt -> amt.multiply(new BigDecimal("0.05")))

            // AGGREGATION: Jumlahkan semua nilai cashback menjadi satu total
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Output
        System.out.println("Total cashback yang diproses: Rp " +
            totalCashback.setScale(2, RoundingMode.HALF_UP));
    }
}
