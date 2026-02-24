# BluBCA Stream API Demo

## Apa itu Stream API?

Stream API adalah **conveyor belt** untuk data. Bayangkan pabrik:

1. **Barang masuk** ke conveyor belt → `.stream()`
2. **Quality control** menyingkirkan yang cacat → `.filter()`
3. **Mesin** mengubah bentuk barang → `.map()`
4. **Penghitung** menjumlahkan hasil akhir → `.reduce()`

Keunggulannya: kita bilang **apa yang mau dilakukan**, bukan **bagaimana caranya** (tidak perlu loop manual, index, variabel temp).

### Pipeline Pattern:
```
data.stream()
    .filter(...)   // Saring: ambil yang memenuhi syarat
    .map(...)      // Ubah: transformasi tiap item
    .reduce(...)   // Gabung: jadikan satu hasil akhir
```

### Bonus: `peek()` untuk Debugging
`peek()` = mengintip data di tengah pipeline **tanpa mengubah apapun**. Sangat berguna untuk melihat apa yang terjadi di setiap langkah.

## Lihat di Kode

| Baris | Apa yang Terjadi |
|-------|-----------------|
| 17 | `record CashbackTrx` — data class: acc_no, trx_amt, balance |
| 24 | `ambilTransaksi()` — ambil data dari PostgreSQL |
| 38 | `.peek()` — tampilkan semua transaksi yang masuk |
| 41 | `.filter()` — ambil transaksi > 100 ribu (syarat cashback) |
| 46 | `.map()` — hitung cashback 5% dari tiap `trx_amt` |
| 51 | `.reduce()` — jumlahkan semua cashback menjadi total |

## Setup Database

```bash
psql -U postgres -c "CREATE DATABASE blu_db;"
psql -U postgres -d blu_db -f init.sql
```

Tabel `blu_cashback_trx`:

| acc_no | trx_amt | balance |
|--------|---------|---------|
| 880012345678 | 50,000 | 2,450,000 |
| 880098765432 | 200,000 | 5,800,000 |
| 880011223344 | 1,500,000 | 12,300,000 |
| 880055667788 | 75,000 | 925,000 |
| 880099887766 | 300,000 | 3,700,000 |

## Contoh Output

```
Berhasil terhubung ke PostgreSQL.

Total transaksi dari database: 5

[Masuk] 880012345678 | Rp 50000.00 | Saldo: Rp 2450000.00
[Masuk] 880098765432 | Rp 200000.00 | Saldo: Rp 5800000.00
[Lolos] 880098765432 | Rp 200000.00
[Cashback 5%] Rp 10000.00
[Masuk] 880011223344 | Rp 1500000.00 | Saldo: Rp 12300000.00
[Lolos] 880011223344 | Rp 1500000.00
[Cashback 5%] Rp 75000.00
[Masuk] 880055667788 | Rp 75000.00 | Saldo: Rp 925000.00
[Masuk] 880099887766 | Rp 300000.00 | Saldo: Rp 3700000.00
[Lolos] 880099887766 | Rp 300000.00
[Cashback 5%] Rp 15000.00

>> Total cashback: Rp 100000.00
```

## 💡 Coba Sendiri

1. Tambah data baru di `init.sql` dengan `trx_amt` = 5000000, jalankan lagi
2. Ubah syarat cashback dari > 100 ribu jadi > 500 ribu
3. Ganti persentase cashback dari 5% jadi 10%

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaCashbackSystem"
```
