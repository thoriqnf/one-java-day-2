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
| 27 | `.filter()` — ambil transaksi > 100 ribu (syarat cashback) |
| 30 | `.peek()` — cetak transaksi yang lolos filter (debugging) |
| 33 | `.map()` — hitung cashback 5% dari tiap transaksi |
| 36 | `.peek()` — cetak nilai cashback per transaksi |
| 40 | `.reduce()` — jumlahkan semua cashback menjadi total |

## Contoh Output

```
=== BlueBCA Cashback Processor ===

[Filter lolos] Rp 200000
[Cashback 5%]  Rp 10000.00
[Filter lolos] Rp 1500000
[Cashback 5%]  Rp 75000.00
[Filter lolos] Rp 300000
[Cashback 5%]  Rp 15000.00

>> Total cashback: Rp 100000.00
```

## 💡 Coba Sendiri

1. Ubah syarat cashback jadi transaksi > 500 ribu — berapa total cashback?
2. Ganti persentase cashback dari 5% jadi 10%
3. Hapus `.peek()` — program tetap jalan, tapi kamu tidak bisa lihat prosesnya!

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaCashbackSystem"
```
