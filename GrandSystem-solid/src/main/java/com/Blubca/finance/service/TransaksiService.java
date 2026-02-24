package com.Blubca.finance.service;

import com.Blubca.finance.domain.Transaksi;
import com.Blubca.finance.repository.TransaksiRepository;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * SERVICE LAYER
 * SOLID: Single Responsibility Principle (SRP).
 * Class ini HANYA fokus pada logika bisnis (Stream, Concurrency, perhitungan).
 * SOLID: Dependency Inversion Principle (DIP).
 * Class ini rely pada abtraksi TransaksiRepository, bukan langsung ke DB_URL.
 */
public class TransaksiService {

    // Dependency Injection (menggunakan interface, bukan implementasi)
    private final TransaksiRepository repository;

    private final AtomicInteger totalTransaksiDiproses = new AtomicInteger(0);
    private final AtomicLong totalNominalDiproses = new AtomicLong(0);

    public TransaksiService(TransaksiRepository repository) {
        this.repository = repository;
    }

    public void jalankanSemuaDemo() {
        // Ambil data lewat interface repository (DIP applied)
        List<Transaksi> transaksiList = repository.findAll();

        if (transaksiList.isEmpty()) {
            System.out.println("Tidak ada data transaksi. Pastikan database sudah diisi.");
            return;
        }

        System.out.println("Total transaksi diproses: " + transaksiList.size() + "\n");

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
    }

    private void demoLambdaFunctional(List<Transaksi> list) {
        Predicate<Transaksi> isSuccess = t -> t.status().equals("SUCCESS");
        Function<Transaksi, String> formatNama = t -> t.customerName().toUpperCase();
        Consumer<Transaksi> cetakTransaksi = t ->
            System.out.println("  [" + t.status() + "] " + t.customerName()
                + " -> Rp " + String.format("%,.2f", t.amount()));

        System.out.println("Transaksi SUCCESS saja:");
        list.stream().filter(isSuccess).forEach(cetakTransaksi);

        System.out.println("\nNama customer (UPPERCASE):");
        list.stream().map(formatNama).forEach(nama -> System.out.println("  " + nama));

        Supplier<String> pesanDefault = () -> "Tidak ada transaksi ditemukan";
        Optional<Transaksi> pertama = list.stream().findFirst();
        String namaCustomer = pertama.map(Transaksi::customerName).orElseGet(pesanDefault);
        System.out.println("\nCustomer pertama: " + namaCustomer);
    }

    private void demoStreamAPI(List<Transaksi> list) {
        double totalAmount = list.stream().mapToDouble(Transaksi::amount).sum();
        System.out.println("Total amount seluruh transaksi: Rp " + String.format("%,.2f", totalAmount));

        OptionalDouble rataRata = list.stream().mapToDouble(Transaksi::amount).average();
        rataRata.ifPresent(avg -> System.out.println("Rata-rata amount: Rp " + String.format("%,.2f", avg)));

        Optional<Transaksi> maxTransaksi = list.stream().max(Comparator.comparingDouble(Transaksi::amount));
        maxTransaksi.ifPresent(t -> System.out.println("Transaksi tertinggi: " + t.customerName() + " -> Rp " + String.format("%,.2f", t.amount())));

        Map<String, List<Transaksi>> grouped = list.stream().collect(Collectors.groupingBy(Transaksi::status));
        System.out.println("\nGrouping berdasarkan status:");
        grouped.forEach((status, trans) -> System.out.println("  " + status + ": " + trans.size() + " transaksi"));

        Map<String, Double> namaAmount = list.stream().collect(Collectors.toMap(
            Transaksi::customerName, Transaksi::amount, Double::sum
        ));
        System.out.println("\nMapping nama -> amount:");
        namaAmount.forEach((nama, amount) -> System.out.println("  " + nama + ": Rp " + String.format("%,.2f", amount)));
    }

    private void demoParallelStream(List<Transaksi> list) {
        long startSequential = System.nanoTime();
        double totalSequential = list.stream().mapToDouble(Transaksi::amount).sum();
        long endSequential = System.nanoTime();

        long startParallel = System.nanoTime();
        double totalParallel = list.parallelStream().mapToDouble(Transaksi::amount).sum();
        long endParallel = System.nanoTime();

        System.out.println("Sequential sum: Rp " + String.format("%,.2f", totalSequential) + " (waktu: " + (endSequential - startSequential) + " ns)");
        System.out.println("Parallel sum:   Rp " + String.format("%,.2f", totalParallel) + " (waktu: " + (endParallel - startParallel) + " ns)");

        List<String> namaSuccess = list.parallelStream()
            .filter(t -> t.status().equals("SUCCESS"))
            .map(Transaksi::customerName)
            .collect(Collectors.toList());
        System.out.println("Nama customer SUCCESS (parallel): " + namaSuccess);
    }

    private void demoConcurrency(List<Transaksi> list) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<Long> futureCountSuccess = executor.submit(() ->
            list.stream().filter(t -> t.status().equals("SUCCESS")).count()
        );

        Future<Double> futureTotalPending = executor.submit(() ->
            list.stream().filter(t -> t.status().equals("PENDING")).mapToDouble(Transaksi::amount).sum()
        );

        Future<String> futureTopCustomer = executor.submit(() ->
            list.stream().max(Comparator.comparingDouble(Transaksi::amount)).map(Transaksi::customerName).orElse("N/A")
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

    private void demoAtomic(List<Transaksi> list) {
        totalTransaksiDiproses.set(0);
        totalNominalDiproses.set(0);

        list.parallelStream().forEach(t -> {
            totalTransaksiDiproses.incrementAndGet();
            totalNominalDiproses.addAndGet((long) t.amount());
        });

        System.out.println("Total transaksi diproses (Atomic): " + totalTransaksiDiproses.get());
        System.out.println("Total nominal diproses (Atomic): Rp " + String.format("%,.2f", (double) totalNominalDiproses.get()));

        AtomicReference<Transaksi> tertinggi = new AtomicReference<>(list.get(0));
        list.parallelStream().forEach(t ->
            tertinggi.updateAndGet(current -> t.amount() > current.amount() ? t : current)
        );
        System.out.println("Transaksi tertinggi (AtomicReference): " + tertinggi.get().customerName() + " -> Rp " + String.format("%,.2f", tertinggi.get().amount()));
    }
}
