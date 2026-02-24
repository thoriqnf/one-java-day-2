package com.Blubca.finance;

import java.util.stream.LongStream;

public class BlueBcaAuditSystem {

    // STEP 1: Logika bisnis dipisahkan dari main
    // Kenapa? Supaya bisa dipanggil ulang, di-test, dan dipakai di tempat lain.
    // Di dunia kerja, main() hanya sebagai "pintu masuk", logika ada di method sendiri.
    public long performParallelAudit(long startId, long endId) {
        System.out.println("[Sistem] Memulai audit paralel dari ID " + startId + " ke " + endId);

        return LongStream.rangeClosed(startId, endId)
            .parallel()
            .filter(id -> id % 1_000_000 == 0)
            .count();
    }

    public void printHeader() {
        System.out.println("============================================");
        System.out.println("   BLUEBCA DIGITAL AUDIT REPORT 2026     ");
        System.out.println("============================================");
    }

    // STEP 2: Main hanya sebagai orkestrator — memanggil fungsi-fungsi di atas
    // `new BlueBcaAuditSystem()` = membuat objek agar bisa panggil method non-static
    public static void main(String[] args) {
        BlueBcaAuditSystem auditApp = new BlueBcaAuditSystem();
        auditApp.printHeader();

        long startTime = System.currentTimeMillis();
        long totalKritikal = auditApp.performParallelAudit(1, 10_000_000);
        long endTime = System.currentTimeMillis();

        System.out.println("\nHasil Audit:");
        System.out.println("- Total Transaksi Diperiksa : 10.000.000");
        System.out.println("- Transaksi Kritikal Ditemukan: " + totalKritikal);
        System.out.println("- Waktu Eksekusi            : " + (endTime - startTime) + " ms");
        System.out.println("============================================");
    }
}
