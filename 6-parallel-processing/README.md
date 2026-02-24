# BluBCA Parallel Processing Demo

## Apa itu Parallel Stream?

CPU komputer modern punya **banyak core** (biasanya 4-16). Secara default, Java hanya pakai 1 core. Dengan `.parallel()`, Java **membagi pekerjaan** ke semua core sekaligus.

Bayangkan antrean supermarket:
- **Sequential** = 1 kasir melayani 10 juta orang sendirian 😰
- **Parallel** = 4 kasir bagi antrean, masing-masing cek 2.5 juta orang 🚀

### Kapan pakai Parallel?
- ✅ Data BANYAK (jutaan item)
- ✅ Operasi INDEPENDEN (setiap item diproses sendiri-sendiri)
- ❌ Data sedikit — overhead parallel malah lebih lambat
- ❌ Operasi bergantung urutan — parallel tidak menjamin urutan

## 2 Versi Demo

### Versi 1: `BlueBcaParallelSystem` (Perbandingan Langsung)
Jalankan operasi yang **sama** dua kali — sequential vs parallel — dan bandingkan waktunya.

### Versi 2: `BlueBcaAuditSystem` (Terstruktur)
Logika bisnis dipisahkan ke method sendiri. Ini pola yang dipakai di dunia kerja: `main()` hanya orkestrator.

## Lihat di Kode (ParallelSystem)

| Baris | Apa yang Terjadi |
|-------|-----------------|
| 17-19 | Sequential stream — `LongStream` tanpa `.parallel()` |
| 32-35 | Parallel stream — tambah `.parallel()` = pakai semua core |
| 42-44 | Cetak perbandingan waktu: sequential vs parallel |

## Contoh Output

```
=== BlueBCA Large Scale Data Audit ===

--- Round 1: Sequential (1 Core) ---
Ditemukan: 10 transaksi kritikal
Waktu: 120 ms

--- Round 2: Parallel (Semua Core) ---
Ditemukan: 10 transaksi kritikal
Waktu: 35 ms

>> Parallel lebih cepat! (Sequential: 120ms vs Parallel: 35ms)
```

> ⚠️ Angka waktu akan berbeda tergantung CPU kamu. Yang penting: parallel **lebih cepat** untuk data besar.

## 💡 Coba Sendiri

1. Ubah 10 juta jadi 100 juta — gap waktu makin terasa!
2. Ubah 10 juta jadi 100 — parallel malah lebih lambat (overhead!)
3. Hapus `.parallel()` dari ParallelSystem — sekarang dua-duanya sequential

## Cara Menjalankan

**Versi Perbandingan:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaParallelSystem"
```

**Versi Terstruktur:**
```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaAuditSystem"
```
