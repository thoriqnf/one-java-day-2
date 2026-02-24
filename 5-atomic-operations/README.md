# BluBCA Atomic Operations Demo

## Apa itu Atomic Operations?

Bayangkan mesin antrean di bank. Ketika 2 orang pencet tombol **bersamaan**:

- ❌ **Tanpa Atomic**: Mesin baca "5", dua-duanya dapat nomor "6". Ada nomor dobel, total salah!
- ✅ **Dengan Atomic**: Mesin **kunci** dulu → baca "5" → kasih "6" → buka → baru orang kedua dapat "7". Selalu benar!

**`int++` itu TIDAK atomic** — sebenarnya ada 3 langkah: baca → tambah → tulis. Thread lain bisa menyela di antara langkah-langkah itu.

**`AtomicInteger.incrementAndGet()`** melakukan 3 langkah itu dalam **satu operasi tak terpisahkan**. Tidak bisa diinterupsi.

### Perbandingan:
```
int count = 0;
count++;                       // BAHAYA: 3 langkah, bisa diinterupsi

AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();       // AMAN: 1 operasi atomic
```

## Lihat di Kode

| Baris | Apa yang Terjadi |
|-------|-----------------|
| 11 | `int unsafeCounter` — counter biasa (TIDAK thread-safe) |
| 14 | `AtomicInteger safeCounter` — counter atomic (SELALU thread-safe) |
| 29 | `unsafeCounter++` — race condition! 10 thread rebutan |
| 50 | `safeCounter.incrementAndGet()` — aman, tidak ada data hilang |

## Contoh Output

```
=== BlueBCA Digital Queue System ===

--- Round 1: UNSAFE (int biasa) ---
Hasil unsafe (harusnya 1000): 987
Benar? TIDAK — race condition!

--- Round 2: SAFE (AtomicInteger) ---
Hasil safe (harusnya 1000):   1000
Benar? YA — selalu akurat!
```

> ⚠️ Angka unsafe akan **berbeda setiap kali dijalankan** — itu yang namanya race condition!

## 💡 Coba Sendiri

1. Jalankan 3 kali berturut-turut — perhatikan angka unsafe selalu beda, safe selalu 1000
2. Ubah jumlah dari 1000 jadi 100000 — gap unsafe makin besar!
3. Ubah pool jadi `newFixedThreadPool(1)` — unsafe jadi benar karena hanya 1 thread

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaAtomicSystem"
```
