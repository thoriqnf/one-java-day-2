package com.Blubca.finance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// STEP 1: Functional Interface — ini seperti "kontrak" 1 fungsi
// Siapapun yang mengimplementasi kontrak ini HARUS menyediakan method test().
// Aturan: @FunctionalInterface = hanya boleh punya 1 abstract method.
@FunctionalInterface
interface BluTransactionRule {
    boolean test(BigDecimal amount);
}

public class BluBcaGrandSystem {

    public static void main(String[] args) {

        // STEP 2: Data transaksi dummy (simulasi dari database)
        List<BigDecimal> transactions = Arrays.asList(
            new BigDecimal("500000"),       // 500 ribu
            new BigDecimal("15000000"),     // 15 juta
            new BigDecimal("250000"),       // 250 ribu
            new BigDecimal("125000000"),    // 125 juta
            new BigDecimal("750000")        // 750 ribu
        );

        // STEP 3: Lambda — implementasi kontrak di atas dalam 1 baris
        // Tanpa lambda, kita harus buat class baru + override method. Ribet!
        // Dengan lambda: (input) -> logika. Selesai.
        BigDecimal highLimit = new BigDecimal("10000000");
        BluTransactionRule isHighValue = (amount) -> amount.compareTo(highLimit) > 0;

        // Kontrak yang SAMA bisa dipakai untuk aturan berbeda — ini kekuatan interface!
        BigDecimal microLimit = new BigDecimal("300000");
        BluTransactionRule isMicroTransaction = (amount) -> amount.compareTo(microLimit) < 0;

        System.out.println("=== BluBCA Digital Transaction Monitor ===");
        System.out.println("Status: Mengaktifkan filter keamanan...\n");

        // STEP 4: Stream API Pipeline — bayangkan conveyor belt
        // Data masuk → difilter → dikumpulkan. Tidak perlu loop manual.
        List<BigDecimal> flaggedTransactions = transactions.stream()
            .filter(isHighValue::test)        // Ambil yang > 10 juta saja
            .collect(Collectors.toList());     // Kumpulkan hasil ke List baru

        if (flaggedTransactions.isEmpty()) {
            System.out.println("Semua transaksi aman.");
        } else {
            System.out.println("Transaksi butuh review Manajemen (> 10 Juta):");
            flaggedTransactions.forEach(t -> System.out.println(">> Flagged: Rp " + t));
        }

        // STEP 5: Reduce — agregasi (jumlahkan semua) dalam satu baris
        // BigDecimal.ZERO = mulai dari 0, BigDecimal::add = cara menjumlahkan
        BigDecimal totalFlagged = flaggedTransactions.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTotal Dana High Value: Rp " + totalFlagged);

        // STEP 6: Pakai aturan kedua — interface yang sama, logika berbeda
        List<BigDecimal> microTransactions = transactions.stream()
            .filter(isMicroTransaction::test)  // Ambil yang < 300 ribu
            .collect(Collectors.toList());
        System.out.println("\nTransaksi Mikro (< 300 Ribu):");
        microTransactions.forEach(t -> System.out.println(">> Micro: Rp " + t));
        System.out.println("----------------------------------------------");
    }
}
