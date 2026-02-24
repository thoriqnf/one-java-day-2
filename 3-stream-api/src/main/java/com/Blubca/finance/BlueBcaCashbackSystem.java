package com.Blubca.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlueBcaCashbackSystem {

    // ==================== KONFIGURASI DATABASE ====================
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    // Record = data class modern Java. Otomatis punya constructor + getter.
    record CashbackTrx(String accNo, BigDecimal trxAmt, BigDecimal balance) {}

    public static void main(String[] args) {

        System.out.println("=== BlueBCA Cashback Processor ===\n");

        // STEP 1: Ambil data transaksi dari PostgreSQL
        List<CashbackTrx> transactions = ambilTransaksi();

        if (transactions.isEmpty()) {
            System.out.println("Tidak ada data. Pastikan database sudah diisi (lihat init.sql).");
            return;
        }

        System.out.println("Total transaksi dari database: " + transactions.size() + "\n");

        // STEP 2: Pipeline lengkap — Filter → Peek → Map → Peek → Reduce
        // Bayangkan conveyor belt: barang masuk → dicek → diproses → dijumlahkan
        BigDecimal totalCashback = transactions.stream()

            // PEEK: Tampilkan semua transaksi yang masuk
            .peek(trx -> System.out.println("[Masuk] " + trx.accNo() + " | Rp " + trx.trxAmt() + " | Saldo: Rp " + trx.balance()))

            // FILTER: Ambil transaksi > 100.000 saja (syarat cashback)
            .filter(trx -> trx.trxAmt().compareTo(new BigDecimal("100000")) > 0)

            .peek(trx -> System.out.println("[Lolos] " + trx.accNo() + " | Rp " + trx.trxAmt()))

            // MAP: Transformasi — hitung cashback 5% dari trx_amt
            .map(trx -> trx.trxAmt().multiply(new BigDecimal("0.05")))

            .peek(cb -> System.out.println("[Cashback 5%] Rp " + cb.setScale(2, RoundingMode.HALF_UP)))

            // REDUCE: Jumlahkan semua cashback jadi satu total
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // STEP 3: Output hasil akhir
        System.out.println("\n>> Total cashback: Rp " +
            totalCashback.setScale(2, RoundingMode.HALF_UP));
    }

    // ==================== KONEKSI DATABASE ====================
    private static List<CashbackTrx> ambilTransaksi() {
        List<CashbackTrx> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Query data
            String sql = "SELECT acc_no, trx_amt, balance FROM blu_cashback_trx";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(new CashbackTrx(
                        rs.getString("acc_no"),
                        rs.getBigDecimal("trx_amt"),
                        rs.getBigDecimal("balance")
                    ));
                }
            }
            System.out.println("Berhasil terhubung ke PostgreSQL.\n");

        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
        }
        return list;
    }

}
