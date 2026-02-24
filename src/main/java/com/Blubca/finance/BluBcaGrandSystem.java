package com.Blubca.finance;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * BluBcaGrandSystem — Kasus untuk menggabungkan semua konsep:
 * Dasar Functional, Lambda, Stream, Concurrency, Atomic, dan Parallel
 * ke dalam arsitektur JPMS dan integrasi PostgreSQL.
 */
public class BluBcaGrandSystem {

    // ==================== KONFIGURASI DATABASE ====================
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    // ==================== ATOMIC VARIABLES ====================
    // Menggunakan Atomic untuk thread-safe counter
    private static final AtomicInteger totalTransaksiDiproses = new AtomicInteger(0);
    private static final AtomicLong totalNominalDiproses = new AtomicLong(0);

    // ==================== RECORD UNTUK DATA TRANSAKSI ====================
    record Transaksi(int id, String customerName, double amount, String status) {}

    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   SELAMAT DATANG DI BluBCA GRAND SYSTEM");
        System.out.println("   JPMS + Functional + Stream + Concurrency");
        System.out.println("==============================================\n");

        // 1. Ambil data dari PostgreSQL
        List<Transaksi> transaksiList = ambilSemuaTransaksi();

        if (transaksiList.isEmpty()) {
            System.out.println("Tidak ada data transaksi. Pastikan database sudah diisi.");
            return;
        }

        System.out.println("Total transaksi dari database: " + transaksiList.size() + "\n");

        // 2. LAMBDA & FUNCTIONAL INTERFACE
        System.out.println("========== LAMBDA & FUNCTIONAL INTERFACE ==========");
        demoLambdaFunctional(transaksiList);

        // 3. STREAM API
        System.out.println("\n========== STREAM API ==========");
        demoStreamAPI(transaksiList);

        // 4. PARALLEL STREAM
        System.out.println("\n========== PARALLEL STREAM ==========");
        demoParallelStream(transaksiList);

        // 5. CONCURRENCY (ExecutorService)
        System.out.println("\n========== CONCURRENCY (ExecutorService) ==========");
        demoConcurrency(transaksiList);

        // 6. ATOMIC VARIABLES
        System.out.println("\n========== ATOMIC VARIABLES ==========");
        demoAtomic(transaksiList);

        System.out.println("\n==============================================");
        System.out.println("   SEMUA DEMO SELESAI!");
        System.out.println("==============================================");
    }

    // ==================== 1. KONEKSI DATABASE ====================
    private static List<Transaksi> ambilSemuaTransaksi() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT id, customer_name, amount, status FROM Blu_transactions";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Transaksi(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getDouble("amount"),
                    rs.getString("status")
                ));
            }
            System.out.println("Berhasil terhubung ke PostgreSQL dan mengambil data.\n");

        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
        }
        return list;
    }

    // ==================== 2. LAMBDA & FUNCTIONAL ====================
    private static void demoLambdaFunctional(List<Transaksi> list) {

        // Predicate — filter transaksi SUCCESS
        Predicate<Transaksi> isSuccess = t -> t.status().equals("SUCCESS");

        // Function — format nama customer menjadi uppercase
        Function<Transaksi, String> formatNama = t -> t.customerName().toUpperCase();

        // Consumer — cetak informasi transaksi
        Consumer<Transaksi> cetakTransaksi = t ->
            System.out.println("  [" + t.status() + "] " + t.customerName()
                + " -> Rp " + String.format("%,.2f", t.amount()));

        System.out.println("Transaksi SUCCESS saja:");
        list.stream()
            .filter(isSuccess)
            .forEach(cetakTransaksi);

        System.out.println("\nNama customer (UPPERCASE):");
        list.stream()
            .map(formatNama)
            .forEach(nama -> System.out.println("  " + nama));

        // Supplier — memberi pesan default
        Supplier<String> pesanDefault = () -> "Tidak ada transaksi ditemukan";
        Optional<Transaksi> pertama = list.stream().findFirst();
        String namaCustomer = pertama.map(Transaksi::customerName).orElseGet(pesanDefault);
        System.out.println("\nCustomer pertama: " + namaCustomer);
    }

    // ==================== 3. STREAM API ====================
    private static void demoStreamAPI(List<Transaksi> list) {

        // Total amount seluruh transaksi
        double totalAmount = list.stream()
            .mapToDouble(Transaksi::amount)
            .sum();
        System.out.println("Total amount seluruh transaksi: Rp " + String.format("%,.2f", totalAmount));

        // Rata-rata amount
        OptionalDouble rataRata = list.stream()
            .mapToDouble(Transaksi::amount)
            .average();
        rataRata.ifPresent(avg ->
            System.out.println("Rata-rata amount: Rp " + String.format("%,.2f", avg)));

        // Transaksi dengan amount tertinggi
        Optional<Transaksi> maxTransaksi = list.stream()
            .max(Comparator.comparingDouble(Transaksi::amount));
        maxTransaksi.ifPresent(t ->
            System.out.println("Transaksi tertinggi: " + t.customerName()
                + " -> Rp " + String.format("%,.2f", t.amount())));

        // Grouping berdasarkan status
        Map<String, List<Transaksi>> grouped = list.stream()
            .collect(Collectors.groupingBy(Transaksi::status));
        System.out.println("\nGrouping berdasarkan status:");
        grouped.forEach((status, trans) ->
            System.out.println("  " + status + ": " + trans.size() + " transaksi"));

        // Collecting ke Map: nama -> amount
        Map<String, Double> namaAmount = list.stream()
            .collect(Collectors.toMap(
                Transaksi::customerName,
                Transaksi::amount,
                Double::sum
            ));
        System.out.println("\nMapping nama -> amount:");
        namaAmount.forEach((nama, amount) ->
            System.out.println("  " + nama + ": Rp " + String.format("%,.2f", amount)));
    }

    // ==================== 4. PARALLEL STREAM ====================
    private static void demoParallelStream(List<Transaksi> list) {

        long startSequential = System.nanoTime();
        double totalSequential = list.stream()
            .mapToDouble(Transaksi::amount)
            .sum();
        long endSequential = System.nanoTime();

        long startParallel = System.nanoTime();
        double totalParallel = list.parallelStream()
            .mapToDouble(Transaksi::amount)
            .sum();
        long endParallel = System.nanoTime();

        System.out.println("Sequential sum: Rp " + String.format("%,.2f", totalSequential)
            + " (waktu: " + (endSequential - startSequential) + " ns)");
        System.out.println("Parallel sum:   Rp " + String.format("%,.2f", totalParallel)
            + " (waktu: " + (endParallel - startParallel) + " ns)");

        // Parallel filter + collect
        List<String> namaSuccess = list.parallelStream()
            .filter(t -> t.status().equals("SUCCESS"))
            .map(Transaksi::customerName)
            .collect(Collectors.toList());
        System.out.println("Nama customer SUCCESS (parallel): " + namaSuccess);
    }

    // ==================== 5. CONCURRENCY ====================
    private static void demoConcurrency(List<Transaksi> list) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Task 1: Hitung total transaksi SUCCESS
        Future<Long> futureCountSuccess = executor.submit(() ->
            list.stream().filter(t -> t.status().equals("SUCCESS")).count()
        );

        // Task 2: Hitung total amount PENDING
        Future<Double> futureTotalPending = executor.submit(() ->
            list.stream()
                .filter(t -> t.status().equals("PENDING"))
                .mapToDouble(Transaksi::amount)
                .sum()
        );

        // Task 3: Ambil nama customer dengan amount tertinggi
        Future<String> futureTopCustomer = executor.submit(() ->
            list.stream()
                .max(Comparator.comparingDouble(Transaksi::amount))
                .map(Transaksi::customerName)
                .orElse("N/A")
        );

        try {
            System.out.println("Jumlah transaksi SUCCESS: " + futureCountSuccess.get());
            System.out.println("Total amount PENDING: Rp " + String.format("%,.2f", futureTotalPending.get()));
            System.out.println("Customer dengan amount tertinggi: " + futureTopCustomer.get());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error pada concurrency: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== 6. ATOMIC ====================
    private static void demoAtomic(List<Transaksi> list) {
        // Reset counter
        totalTransaksiDiproses.set(0);
        totalNominalDiproses.set(0);

        // Simulasi pemrosesan concurrent menggunakan parallelStream + atomic
        list.parallelStream().forEach(t -> {
            totalTransaksiDiproses.incrementAndGet();
            totalNominalDiproses.addAndGet((long) t.amount());
        });

        System.out.println("Total transaksi diproses (Atomic): " + totalTransaksiDiproses.get());
        System.out.println("Total nominal diproses (Atomic): Rp " + String.format("%,.2f", (double) totalNominalDiproses.get()));

        // AtomicReference untuk menyimpan transaksi tertinggi secara thread-safe
        AtomicReference<Transaksi> tertinggi = new AtomicReference<>(list.get(0));
        list.parallelStream().forEach(t -> {
            tertinggi.updateAndGet(current ->
                t.amount() > current.amount() ? t : current
            );
        });
        System.out.println("Transaksi tertinggi (AtomicReference): "
            + tertinggi.get().customerName()
            + " -> Rp " + String.format("%,.2f", tertinggi.get().amount()));
    }
}
