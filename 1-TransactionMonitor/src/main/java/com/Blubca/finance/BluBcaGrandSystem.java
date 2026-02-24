package com.Blubca.finance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 01. Functional Interface: Kontrak aturan transaksi BluBCA
@FunctionalInterface
interface BluTransactionRule {
    boolean test(BigDecimal amount);
}

public class BluBcaGrandSystem {

    public static void main(String[] args) {

        // 02. Lambda: Membuat data transaksi dummy (simulasi dari database)
        List<BigDecimal> transactions = Arrays.asList(
            new BigDecimal("500000"),
            new BigDecimal("15000000"),
            new BigDecimal("250000"),
            new BigDecimal("125000000"),
            new BigDecimal("750000")
        );

        // Aturan: Transaksi di atas 10 Juta dianggap 'High Value' dan butuh verifikasi ekstra.
        BigDecimal limit = new BigDecimal("10000000");
        BluTransactionRule isHighValue = (amount) -> amount.compareTo(limit) > 0;

        System.out.println("=== BluBCA Digital Transaction Monitor ===");
        System.out.println("Status: Mengaktifkan filter keamanan...\n");

        // 03. Stream API Pipeline: Mengolah data secara deklaratif (elegan)
        // memfilter data berdasarkan aturan, lalu mengumpulkannya ke dalam list baru.
        List<BigDecimal> flaggedTransactions = transactions.stream()
            .filter(isHighValue::test) // What to do: Filter yang nilainya tinggi
            .collect(Collectors.toList());

        // Menampilkan hasil menggunakan method reference (fitur modern Java lainnya)
        if (flaggedTransactions.isEmpty()) {
            System.out.println("Semua transaksi aman.");
        } else {
            System.out.println("Daftar transaksi yang memerlukan review Manajemen BluBCA:");
            flaggedTransactions.forEach(t -> System.out.println(">> Flagged: Rp " + t));
        }

        // Contoh Aggregation: Menghitung total dana yang masuk kategori High Value
        BigDecimal totalFlagged = flaggedTransactions.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\nTotal Dana High Value: Rp " + totalFlagged);
        System.out.println("----------------------------------------------");
    }
}
