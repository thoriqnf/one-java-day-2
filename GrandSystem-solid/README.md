# BluBCA Grand System (Clean Architecture + SOLID)

Ini adalah versi refactor dari **GrandSystem** sebelumnya. Fungsionalitasnya **100% sama**, tetapi kode ini distruktur ulang menggunakan **Clean Architecture** dan prinsip **SOLID**.

## Apa yang Berubah?

Sebelumnya, file `BluBcaGrandSystem.java` melakukan *semuanya*: dari koneksi database, query SQL, sampai manipulasi Stream dan formatting string. Ini melanggar prinsip desain yang baik (disebut *God Object*).

Sekarang, kode dibagi menjadi 4 bagian utama yang saling terpisah:

### 1. Domain Layer (`domain/Transaksi.java`)
Hanya berisi struktur data inti menggunakan Java `record`. Data murni, tidak ada SQL, tidak ada logika perhitungan.

### 2. Repository Layer (`repository/`)
* **`TransaksiRepository.java` (Interface)**: Ini adalah *kontrak*. Mendesfinisikan bahwa kita bisa `findAll()`.
* **`PostgresTransaksiRepository.java` (Implementasi)**: Fokus **hanya** pada urusan database (JDBC, ResultSet, tabel otomatis). Class ini mengimplementasikan kontrak di atas.

### 3. Service Layer (`service/TransaksiService.java`)
Fokus **hanya** pada logika bisnis (Stream, Concurrency, Lambda, Atomic).
* **Dependency Inversion (DIP)**: Service ini meminta `TransaksiRepository` (interface), bukan `PostgresTransaksiRepository` secara langsung. Service tidak peduli datanya datang dari PostgreSQL, MySQL, atau File Text; yang penting datanya adalah `List<Transaksi>`.

### 4. Entry Point (`Main.java`)
Tempat kita merakit (wiring) semua bagian di atas menggunakan **Dependency Injection** (injeksi dependensi).

```java
// Main.java merakit komponen:
TransaksiRepository repo = new PostgresTransaksiRepository(); // Bikin alat ambil data (PosgreSQL)
TransaksiService service = new TransaksiService(repo);        // Kasih alat itu ke Service
service.jalankanSemuaDemo();                                  // Suruh Service kerja
```

## Cara Menjalankan

Tidak perlu setup SQL manual. Program akan otomatis ke database `postgres`, membuat tabel `Blu_transactions`, dan mengisi 5 data percontohan jika tabel belum ada.

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.Main"
```
