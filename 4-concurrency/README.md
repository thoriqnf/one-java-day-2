# BluBCA Concurrency Demo

## Apa itu Concurrency & Thread Pool?

**Thread** adalah "pekerja" yang menjalankan tugas. Secara default, Java punya 1 thread (main). Kalau kamu mau kirim 5 notifikasi bersamaan, kamu butuh beberapa pekerja.

**Cara lama**: Bikin `new Thread()` untuk setiap tugas. Masalahnya? Kalau 1000 tugas = 1000 Thread baru. Ini seperti **merekrut 1000 karyawan baru** untuk 1000 kerjaan, lalu pecat mereka semua. Boros!

**Cara modern**: Pakai **Thread Pool** (`ExecutorService`). Bayangkan 3 teller bank yang melayani 100 nasabah **bergantian**. Teller tidak dipecat setelah 1 nasabah — mereka langsung ambil nasabah berikutnya dari antrean.

### Alur Thread Pool:
```
[Tugas 1] ─┐
[Tugas 2] ─┤── Antrean ──→ [Pekerja 1] [Pekerja 2] [Pekerja 3]
[Tugas 3] ─┤                    ↑ ambil tugas baru setelah selesai
[Tugas 4] ─┤
[Tugas 5] ─┘
```

## Lihat di Kode

| Baris | Apa yang Terjadi |
|-------|-----------------|
| 17-24 | Cara lama — `new Thread()` untuk setiap tugas (demo 2 nasabah) |
| 35 | `newFixedThreadPool(3)` — buat pool dengan 3 pekerja tetap |
| 40-53 | `submit(() -> {...})` — kirim tugas ke pool pakai lambda |
| 56 | `shutdown()` — tutup pintu masuk, tidak terima tugas baru |
| 60 | `awaitTermination()` — tunggu semua tugas selesai |

## Contoh Output

```
=== BlueBCA Notification Center ===

--- Cara Lama (Thread Manual) ---
[Thread-0] Kirim ke Nasabah 1
[Thread-1] Kirim ke Nasabah 2

--- Cara Modern (Thread Pool: 3 Pekerja) ---
[pool-1-thread-1] Mengirim notifikasi ke Nasabah ID: 1
[pool-1-thread-2] Mengirim notifikasi ke Nasabah ID: 2
[pool-1-thread-3] Mengirim notifikasi ke Nasabah ID: 3
[pool-1-thread-1] Selesai mengirim ke Nasabah ID: 1
[pool-1-thread-1] Mengirim notifikasi ke Nasabah ID: 4
...
Status: Semua notifikasi BlueBCA telah diproses.
```

> Perhatikan: thread-1 sampai thread-3 bergantian — tidak ada thread-4 atau thread-5!

## 💡 Coba Sendiri

1. Ubah pool jadi `newFixedThreadPool(1)` — apa yang terjadi? (semua sequential!)
2. Ubah jumlah tugas dari 5 jadi 10 — tetap hanya 3 pekerja
3. Hapus `Thread.sleep(1000)` — perhatikan semua selesai hampir instan

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaConcurrencySystem"
```
