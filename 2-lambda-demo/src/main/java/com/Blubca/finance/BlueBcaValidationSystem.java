package com.Blubca.finance;

import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlueBcaValidationSystem {

    public static void main(String[] args) {

        // ====================================================
        // STEP 1: Cara Lama (Anonymous Inner Class)
        // Bayangkan: untuk setiap aturan, kita harus bikin "surat resmi" lengkap.
        // ====================================================
        Predicate<Integer> isUsiaCukupLama = new Predicate<Integer>() {
            @Override
            public boolean test(Integer usia) {
                return usia >= 17;
            }
        };
        // ^ 5 baris untuk logika 1 baris! Ini yang Lambda selesaikan.

        System.out.println("=== BluBCA Account Validation ===\n");
        System.out.println("--- Cara Lama (Anonymous Class) ---");
        System.out.println("Usia 20: " + (isUsiaCukupLama.test(20) ? "LULUS" : "TIDAK LULUS"));
        System.out.println("Usia 15: " + (isUsiaCukupLama.test(15) ? "LULUS" : "TIDAK LULUS"));

        // ====================================================
        // STEP 2: Cara Lambda — logika SAMA, penulisan jauh lebih singkat
        // Format: (input) -> logika
        //         (usia)  -> usia >= 17
        // ====================================================
        Predicate<Integer> isUsiaCukup = (usia) -> usia >= 17;

        System.out.println("\n--- Cara Lambda (1 baris!) ---");
        System.out.println("Usia 20: " + (isUsiaCukup.test(20) ? "LULUS" : "TIDAK LULUS"));
        System.out.println("Usia 15: " + (isUsiaCukup.test(15) ? "LULUS" : "TIDAK LULUS"));

        // ====================================================
        // STEP 3: Predicate — fungsi yang menerima input, return true/false
        // Cocok untuk validasi: "Apakah saldo cukup?", "Apakah usia valid?"
        // ====================================================
        Predicate<Double> isSaldoCukup = (saldo) -> saldo >= 50000;

        System.out.println("\n--- Predicate: Cek Saldo Minimum ---");
        System.out.println("Saldo 100000: " + (isSaldoCukup.test(100000.0) ? "Cukup" : "Kurang"));
        System.out.println("Saldo 30000: " + (isSaldoCukup.test(30000.0) ? "Cukup" : "Kurang"));

        // ====================================================
        // STEP 4: Consumer — fungsi yang menerima input, TIDAK return apa-apa
        // Cocok untuk: cetak, kirim notifikasi, simpan log
        // ====================================================
        Consumer<String> kirimNotifikasi = (pesan) -> System.out.println("[NOTIF] " + pesan);

        System.out.println("\n--- Consumer: Kirim Notifikasi ---");
        kirimNotifikasi.accept("Selamat datang di BluBCA!");
        kirimNotifikasi.accept("Transfer Rp 500.000 berhasil.");

        // ====================================================
        // STEP 5: Function — fungsi yang menerima input, return output BERBEDA
        // Cocok untuk: transformasi data, format, konversi
        // ====================================================
        Function<Double, String> formatRupiah = (amount) -> "Rp " + String.format("%,.0f", amount);

        System.out.println("\n--- Function: Format Ke Rupiah ---");
        System.out.println(formatRupiah.apply(1500000.0));
        System.out.println(formatRupiah.apply(250000.0));

        // ====================================================
        // STEP 6: Runnable Lambda — tanpa input, tanpa output
        // Cocok untuk: tugas sederhana yang tinggal "jalankan"
        // ====================================================
        Runnable greeting = () -> System.out.println("\nTerima kasih telah memilih BluBCA Digital!");
        greeting.run();
    }
}
