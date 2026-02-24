# BluBCA Lambda Demo

## Apa itu Lambda?

Lambda adalah cara **singkat** menulis fungsi di Java. Bayangkan kamu mau bikin aturan sederhana: "Apakah usia >= 17?" 

**Cara lama**: Harus bikin class anonim lengkap — 5 baris untuk logika 1 baris. Seperti menulis surat resmi hanya untuk bilang "Ya" atau "Tidak".

**Cara lambda**: `(usia) -> usia >= 17` — selesai dalam 1 baris. Seperti langsung menjawab di sticky note.

### Format Lambda:
```
(input)  ->  logika
(usia)   ->  usia >= 17
```

## 4 Jenis Lambda Bawaan Java

| Jenis | Format | Gunanya | Contoh di Kode |
|-------|--------|---------|---------------|
| **Predicate** | `input → boolean` | Validasi (ya/tidak) | Cek usia, cek saldo |
| **Consumer** | `input → void` | Aksi tanpa return | Kirim notifikasi |
| **Function** | `input → output` | Transformasi data | Format ke Rupiah |
| **Runnable** | `() → void` | Jalankan saja | Greeting message |

## Lihat di Kode

| Baris | Apa yang Terjadi |
|-------|-----------------|
| 15-20 | Cara lama — `new Predicate<>()` + override. Banyak boilerplate! |
| 33 | Cara lambda — logika yang **sama persis**, cuma 1 baris |
| 43 | `Predicate<Double>` — cek saldo minimum |
| 53 | `Consumer<String>` — terima pesan, cetak notifikasi |
| 63 | `Function<Double, String>` — terima angka, return string Rupiah |
| 71 | `Runnable` — tanpa input, tanpa output, tinggal `.run()` |

## Contoh Output

```
=== BluBCA Account Validation ===

--- Cara Lama (Anonymous Class) ---
Usia 20: LULUS
Usia 15: TIDAK LULUS

--- Cara Lambda (1 baris!) ---
Usia 20: LULUS
Usia 15: TIDAK LULUS

--- Predicate: Cek Saldo Minimum ---
Saldo 100000: Cukup
Saldo 30000: Kurang

--- Consumer: Kirim Notifikasi ---
[NOTIF] Selamat datang di BluBCA!
[NOTIF] Transfer Rp 500.000 berhasil.

--- Function: Format Ke Rupiah ---
Rp 1,500,000
Rp 250,000

Terima kasih telah memilih BluBCA Digital!
```

## 💡 Coba Sendiri

1. Ubah `isUsiaCukup` jadi minimal 21 tahun — jalankan lagi
2. Buat lambda `Predicate<String>` yang cek apakah nama customer lebih dari 5 karakter

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaValidationSystem"
```
