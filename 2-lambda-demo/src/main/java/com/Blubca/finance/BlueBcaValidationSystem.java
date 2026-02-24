package com.Blubca.finance;

import java.util.function.Predicate;

public class BlueBcaValidationSystem {

    public static void main(String[] args) {

        // ====================================================
        // 3. Perbedaan Visual: Cara Lama vs Lambda
        // ====================================================

        // A. Cara Lama (Penuh Side-Effect):
        // Bahaya: Variabel totalPajak di luar bisa diubah siapa saja
        double totalPajak = 0;
        double[] transaksi = {500000, 15000000, 250000, 125000000, 750000};
        for (double t : transaksi) {
            totalPajak += t * 0.01; // 1% pajak
        }
        System.out.println("Cara Lama - Total Pajak: Rp " + totalPajak);

        // B. Cara Lambda (Tanpa Side-Effect):
        // Predicate<Integer>: Memberitahu Java bahwa fungsi ini menerima angka (Integer).
        // (usia): Nama variabel input (bisa bebas, tapi deskriptif lebih baik).
        // ->: Operator Lambda, jembatan antara input dan logika.
        // usia >= 17: Logika bisnisnya.
        // .test(usiaCalon): Ini adalah metode bawaan dari Predicate untuk menjalankan logika Lambda
        Predicate<Integer> isUsiaCukup = (usia) -> usia >= 17;

        int usiaCalon = 20;
        if (isUsiaCukup.test(usiaCalon)) {
            System.out.println("Usia " + usiaCalon + " tahun: LULUS validasi, bisa daftar BluBCA.");
        } else {
            System.out.println("Usia " + usiaCalon + " tahun: TIDAK LULUS, minimal 17 tahun.");
        }

        int usiaCalonMuda = 15;
        if (isUsiaCukup.test(usiaCalonMuda)) {
            System.out.println("Usia " + usiaCalonMuda + " tahun: LULUS validasi, bisa daftar BluBCA.");
        } else {
            System.out.println("Usia " + usiaCalonMuda + " tahun: TIDAK LULUS, minimal 17 tahun.");
        }

        // Contoh Lambda yang lebih kompleks: Pengecekan saldo minimum untuk fitur bluGether
        // (Input) -> (Logika/Output)
        Runnable greeting = () -> System.out.println("\nTerima kasih telah memilih bank digital terbaik.");
        greeting.run();
    }
}
