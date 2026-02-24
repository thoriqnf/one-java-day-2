package com.Blubca.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class BlueBcaCashbackSystem {
    public static void main(String[] args) {

        // STEP 1: Source — daftar transaksi nasabah hari ini
        List<BigDecimal> transactions = Arrays.asList(
            new BigDecimal("50000"),      // 50 ribu  — tidak dapat cashback
            new BigDecimal("200000"),     // 200 ribu — dapat!
            new BigDecimal("1500000"),    // 1.5 juta — dapat!
            new BigDecimal("75000"),      // 75 ribu  — tidak dapat
            new BigDecimal("300000")      // 300 ribu — dapat!
        );

        System.out.println("=== BlueBCA Cashback Processor ===\n");

        // STEP 2: Pipeline lengkap — Filter → Peek → Map → Peek → Reduce
        // Bayangkan conveyor belt: barang masuk → dicek → diproses → dijumlahkan
        BigDecimal totalCashback = transactions.stream()

            // FILTER: Ambil transaksi > 100.000 saja (syarat cashback)
            .filter(amt -> amt.compareTo(new BigDecimal("100000")) > 0)

            // PEEK: Intip hasil filter — ini untuk debugging, tidak mengubah data
            .peek(amt -> System.out.println("[Filter lolos] Rp " + amt))

            // MAP: Transformasi — hitung cashback 5% dari tiap transaksi
            .map(amt -> amt.multiply(new BigDecimal("0.05")))

            // PEEK: Intip hasil map — lihat berapa cashback per transaksi
            .peek(cb -> System.out.println("[Cashback 5%]  Rp " + cb.setScale(2, RoundingMode.HALF_UP)))

            // REDUCE: Jumlahkan semua cashback jadi satu total
            // BigDecimal.ZERO = mulai dari 0, BigDecimal::add = cara menjumlahkan
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // STEP 3: Output hasil akhir
        System.out.println("\n>> Total cashback: Rp " +
            totalCashback.setScale(2, RoundingMode.HALF_UP));
    }
}
