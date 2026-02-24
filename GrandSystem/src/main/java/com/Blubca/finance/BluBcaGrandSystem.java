package com.Blubca.finance;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * BluBcaGrandSystem — "Final Boss": Menggabungkan semua konsep dari Demo 1-6
 * ke dalam satu program yang terhubung ke database PostgreSQL.
 *
 * Konsep: Lambda, Stream, Parallel, Concurrency, Atomic, JDBC, Record, JPMS.
 */
public class BluBcaGrandSystem {

    // ==================== KONFIGURASI DATABASE ====================
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    // ==================== ATOMIC VARIABLES ====================
    // Atomic = thread-safe counter (lihat Demo 5 untuk penjelasan lengkap)
    private static final AtomicInteger totalTransaksiDiproses = new AtomicInteger(0);
    private static final AtomicLong totalNominalDiproses = new AtomicLong(0);

    // ==================== RECORD ====================
    // Record = cara modern Java untuk bikin "data class"
    // Otomatis punya: constructor, getter (id(), customerName(), dll), toString(), equals()
    // Tanpa record, kamu harus tulis 30+ baris boilerplate!
    record Transaksi(int id, String customerName, double amount, String status) {}

    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   SELAMAT DATANG DI BluBCA GRAND SYSTEM");
        System.out.println("   JPMS + Functional + Stream + Concurrency");
        System.out.println("==============================================\n");

        // 1. Ambil data dari PostgreSQL (lihat method di bawah)
        List<Transaksi> transaksiList = ambilSemuaTransaksi();

        if (transaksiList.isEmpty()) {
            System.out.println("Tidak ada data transaksi. Pastikan database sudah diisi.");
            return;
        }

        System.out.println("Total transaksi dari database: " + transaksiList.size() + "\n");

        // 2-6: Jalankan setiap demo section
        System.out.println("========== LAMBDA & FUNCTIONAL INTERFACE ==========");
        demoLambdaFunctional(transaksiList);

        System.out.println("\n========== STREAM API ==========");
        demoStreamAPI(transaksiList);

        System.out.println("\n========== PARALLEL STREAM ==========");
        demoParallelStream(transaksiList);

        System.out.println("\n========== CONCURRENCY (ExecutorService) ==========");
        demoConcurrency(transaksiList);

        System.out.println("\n========== ATOMIC VARIABLES ==========");
        demoAtomic(transaksiList);

        System.out.println("\n==============================================");
        System.out.println("   SEMUA DEMO SELESAI!");
        System.out.println("==============================================");
    }

    // ==================== JDBC: KONEKSI DATABASE ====================
    private static List<Transaksi> ambilSemuaTransaksi() {
        List<Transaksi> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Auto-create table jika belum ada
            buatTabelJikaBelumAda(conn);

            String sql = "SELECT id, customer_name, amount, status FROM Blu_transactions";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(new Transaksi(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                    ));
                }
            }
            System.out.println("Berhasil terhubung ke PostgreSQL.\n");

        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
        }
        return list;
    }

    // Buat tabel + isi data dummy jika belum ada
    private static void buatTabelJikaBelumAda(Connection conn) throws SQLException {
        String createTable = """
            CREATE TABLE IF NOT EXISTS Blu_transactions (
              id SERIAL PRIMARY KEY,
              customer_name VARCHAR(100),
              amount DECIMAL(15, 2),
              status VARCHAR(20)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        }

        // Cek apakah tabel kosong, jika iya isi data seeding
        String countSql = "SELECT COUNT(*) FROM Blu_transactions";
        try (PreparedStatement stmt = conn.prepareStatement(countSql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            if (rs.getInt(1) == 0) {
                String seed = """
                    INSERT INTO Blu_transactions (customer_name, amount, status) VALUES
                    ('Budi Utomo', 500000.00, 'SUCCESS'),
                    ('Siti Aminah', 15000000.00, 'SUCCESS'),
                    ('Andi Wijaya', 250000.00, 'PENDING'),
                    ('Rina Lestari', 125000000.00, 'SUCCESS'),
                    ('Dewi Sartika', 750000.00, 'SUCCESS')
                    """;
                try (Statement seedStmt = conn.createStatement()) {
                    seedStmt.execute(seed);
                    System.out.println("Tabel Blu_transactions dibuat dan diisi 5 data dummy.");
                }
            }
        }
    }

    // ==================== LAMBDA & FUNCTIONAL INTERFACE ====================
    // 4 jenis lambda bawaan Java (sama seperti Demo 2):
    // - Predicate: input → boolean (untuk filter/validasi)
    // - Function:  input → output  (untuk transformasi)
    // - Consumer:  input → void    (untuk aksi/cetak)
    // - Supplier:  ()    → output  (untuk nilai default)
    private static void demoLambdaFunctional(List<Transaksi> list) {

        Predicate<Transaksi> isSuccess = t -> t.status().equals("SUCCESS");
        Function<Transaksi, String> formatNama = t -> t.customerName().toUpperCase();
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

        Supplier<String> pesanDefault = () -> "Tidak ada transaksi ditemukan";
        Optional<Transaksi> pertama = list.stream().findFirst();
        String namaCustomer = pertama.map(Transaksi::customerName).orElseGet(pesanDefault);
        System.out.println("\nCustomer pertama: " + namaCustomer);
    }

    // ==================== STREAM API ====================
    // Stream operations pada data real dari database
    // sum, average, max = terminal operations (menghasilkan nilai akhir)
    // groupingBy, toMap = collectors (mengumpulkan ke struktur data baru)
    private static void demoStreamAPI(List<Transaksi> list) {

        double totalAmount = list.stream()
            .mapToDouble(Transaksi::amount)
            .sum();
        System.out.println("Total amount seluruh transaksi: Rp " + String.format("%,.2f", totalAmount));

        OptionalDouble rataRata = list.stream()
            .mapToDouble(Transaksi::amount)
            .average();
        rataRata.ifPresent(avg ->
            System.out.println("Rata-rata amount: Rp " + String.format("%,.2f", avg)));

        Optional<Transaksi> maxTransaksi = list.stream()
            .max(Comparator.comparingDouble(Transaksi::amount));
        maxTransaksi.ifPresent(t ->
            System.out.println("Transaksi tertinggi: " + t.customerName()
                + " -> Rp " + String.format("%,.2f", t.amount())));

        // groupingBy = kelompokkan berdasarkan field tertentu (seperti GROUP BY di SQL)
        Map<String, List<Transaksi>> grouped = list.stream()
            .collect(Collectors.groupingBy(Transaksi::status));
        System.out.println("\nGrouping berdasarkan status:");
        grouped.forEach((status, trans) ->
            System.out.println("  " + status + ": " + trans.size() + " transaksi"));

        // toMap = buat Map dari stream (key = nama, value = amount)
        Map<String, Double> namaAmount = list.stream()
            .collect(Collectors.toMap(
                Transaksi::customerName,
                Transaksi::amount,
                Double::sum  // Jika nama sama, jumlahkan amount-nya
            ));
        System.out.println("\nMapping nama -> amount:");
        namaAmount.forEach((nama, amount) ->
            System.out.println("  " + nama + ": Rp " + String.format("%,.2f", amount)));
    }

    // ==================== PARALLEL STREAM ====================
    // Sama seperti Demo 6: bandingkan sequential vs parallel pada data real
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

        List<String> namaSuccess = list.parallelStream()
            .filter(t -> t.status().equals("SUCCESS"))
            .map(Transaksi::customerName)
            .collect(Collectors.toList());
        System.out.println("Nama customer SUCCESS (parallel): " + namaSuccess);
    }

    // ==================== CONCURRENCY ====================
    // ExecutorService + Future: jalankan beberapa tugas secara bersamaan
    // Future<T> = "janji" bahwa hasilnya akan tersedia nanti (async)
    // .get() = tunggu dan ambil hasilnya (blocking)
    private static void demoConcurrency(List<Transaksi> list) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 3 tugas berjalan bersamaan di 3 thread berbeda
        Future<Long> futureCountSuccess = executor.submit(() ->
            list.stream().filter(t -> t.status().equals("SUCCESS")).count()
        );

        Future<Double> futureTotalPending = executor.submit(() ->
            list.stream()
                .filter(t -> t.status().equals("PENDING"))
                .mapToDouble(Transaksi::amount)
                .sum()
        );

        Future<String> futureTopCustomer = executor.submit(() ->
            list.stream()
                .max(Comparator.comparingDouble(Transaksi::amount))
                .map(Transaksi::customerName)
                .orElse("N/A")
        );

        try {
            // .get() menunggu hasil dari setiap Future
            System.out.println("Jumlah transaksi SUCCESS: " + futureCountSuccess.get());
            System.out.println("Total amount PENDING: Rp " + String.format("%,.2f", futureTotalPending.get()));
            System.out.println("Customer dengan amount tertinggi: " + futureTopCustomer.get());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error pada concurrency: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== ATOMIC ====================
    // Sama seperti Demo 5: operasi thread-safe tanpa synchronized
    // parallelStream + atomic = pemrosesan parallel yang aman
    private static void demoAtomic(List<Transaksi> list) {
        totalTransaksiDiproses.set(0);
        totalNominalDiproses.set(0);

        list.parallelStream().forEach(t -> {
            totalTransaksiDiproses.incrementAndGet();
            totalNominalDiproses.addAndGet((long) t.amount());
        });

        System.out.println("Total transaksi diproses (Atomic): " + totalTransaksiDiproses.get());
        System.out.println("Total nominal diproses (Atomic): Rp " + String.format("%,.2f", (double) totalNominalDiproses.get()));

        // AtomicReference = atomic untuk object (bukan hanya angka)
        // updateAndGet = baca current → bandingkan → tulis yang menang, secara atomic
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
