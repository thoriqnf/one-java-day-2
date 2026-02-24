package com.Blubca.finance;

import java.util.List;
import java.util.Arrays;
import java.util.stream.LongStream;

public class BlueBcaAuditSystem {
    // 1. Fungsi Logika Bisnis: Menghitung transaksi kritikal secara paralel
    // Fungsi ini dipisahkan dari main agar bisa dipanggil berulang kali
    public long performParallelAudit(long startId, long endId) {
        System.out.println("[Sistem] Memulai audit paralel dari ID: " + startId + " ke " + endId);

        return LongStream.rangeClosed(startId, endId)
            .parallel() // Menggunakan semua core CPU server BlueBCA
            .filter(id -> id % 1_000_000 == 0) // Contoh filter: ambil tiap kelipatan 1 juta
            .count();
    }

    // 2. Fungsi untuk mencetak header laporan
    public void printHeader() {
        System.out.println("============================================");
        System.out.println("   BLUEBCA DIGITAL AUDIT REPORT 2026     ");
        System.out.println("============================================");
    }

    // 3. Main Method: Sebagai entry point (pintu masuk) program
    public static void main(String[] args) {
        // Membuat instance/objek dari kelas itu sendiri
        BlueBcaAuditSystem auditApp = new BlueBcaAuditSystem();
        // Memanggil fungsi-fungsi dari objek tersebut
        auditApp.printHeader();
        long startTime = System.currentTimeMillis();
        // Memanggil fungsi logika bisnis dengan input 10 juta data
        long totalKritikal = auditApp.performParallelAudit(1, 10_000_000);
        long endTime = System.currentTimeMillis();

        // Output hasil akhir
        System.out.println("\nHasil Audit:");
        System.out.println("- Total Transaksi Diperiksa : 10.000.000");
        System.out.println("- Transaksi Kritikal Ditemukan: " + totalKritikal);
        System.out.println("- Waktu Eksekusi            : " + (endTime - startTime) + " ms");
        System.out.println("============================================");
    }
}
