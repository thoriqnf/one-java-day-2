# BluBCA Digital Transaction Monitor

## Apa itu Functional Interface, Lambda & Stream?

**Functional Interface** adalah kontrak yang hanya punya *satu* method. Bayangkan seperti stempel keputusan: "Transaksi ini lolos atau tidak?" — caranya bebas, yang penting jawabannya `true`/`false`.

**Lambda** `(input) -> logika` adalah cara singkat untuk mengisi kontrak itu tanpa bikin class baru. Seperti menulis jawaban langsung di kertas ujian, tanpa perlu bikin buku baru.

**Stream API** adalah conveyor belt: data masuk → difilter → diproses → dikumpulkan. Tidak perlu loop `for` manual.

## Lihat di Kode

| Baris | Apa yang Terjadi |
|-------|-----------------|
| 11-12 | `BluTransactionRule` — kontrak 1 fungsi: terima amount, return boolean |
| 34-35 | `isHighValue` — implementasi kontrak pakai lambda, transaksi > 10 juta |
| 38-39 | `isMicroTransaction` — kontrak **yang sama**, aturan berbeda (< 300 ribu) |
| 44-46 | Stream pipeline: `.stream()` → `.filter()` → `.collect()` |
| 57-58 | `.reduce()` — jumlahkan semua BigDecimal jadi satu total |

## Contoh Output

```
=== BluBCA Digital Transaction Monitor ===
Status: Mengaktifkan filter keamanan...

Transaksi butuh review Manajemen (> 10 Juta):
>> Flagged: Rp 15000000
>> Flagged: Rp 125000000

Total Dana High Value: Rp 140000000

Transaksi Mikro (< 300 Ribu):
>> Micro: Rp 250000
----------------------------------------------
```

## 💡 Coba Sendiri

1. Ubah limit high value jadi 5 juta — apa yang berubah?
2. Tambah transaksi baru `new BigDecimal("999999999")` ke list
3. Buat aturan ketiga: `isMediumValue` untuk transaksi antara 1-10 juta

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BluBcaGrandSystem"
```
