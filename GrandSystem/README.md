# BluBCA Grand System

## Apa itu Grand System?

Ini adalah **"final boss"** — menggabungkan SEMUA konsep dari Demo 1-6 dalam satu program yang terhubung ke database PostgreSQL sungguhan.

> ⚠️ Pastikan kamu sudah paham Demo 1-6 sebelum masuk ke sini!

### Konsep yang Digabungkan:

| No | Konsep | Apa yang Dilakukan di Sini |
|----|--------|--------------------------|
| 1 | **Lambda** | `Predicate`, `Function`, `Consumer`, `Supplier` untuk proses transaksi |
| 2 | **Stream API** | `filter`, `map`, `sum`, `average`, `max`, `groupingBy`, `toMap` |
| 3 | **Parallel Stream** | Perbandingan sequential vs parallel pada data real |
| 4 | **Concurrency** | `ExecutorService` + `Future` + `Callable` — 3 tugas parallel |
| 5 | **Atomic** | `AtomicInteger`, `AtomicLong`, `AtomicReference` untuk thread-safe |
| 6 | **JDBC** | Koneksi PostgreSQL, `PreparedStatement`, `ResultSet` |
| 7 | **Record** | `record Transaksi(...)` — Java modern untuk data class |

## Lihat di Kode

| Baris | Section |
|-------|---------|
| 28 | `record Transaksi` — data class modern (pengganti class + getter) |
| 73-95 | Koneksi database — `DriverManager`, `PreparedStatement`, `ResultSet` |
| 98-126 | Lambda demo — `Predicate`, `Function`, `Consumer`, `Supplier` |
| 129-168 | Stream API — `sum`, `average`, `max`, `groupingBy`, `toMap` |
| 171-196 | Parallel vs Sequential stream comparison |
| 199-232 | Concurrency — `Future<>` untuk 3 tugas parallel |
| 235-259 | Atomic — `AtomicInteger`, `AtomicLong`, `AtomicReference` |

## Setup Database (PostgreSQL)

```bash
# 1. Buat database
psql -U postgres -c "CREATE DATABASE blu_db;"

# 2. Jalankan init.sql
psql -U postgres -d blu_db -f init.sql
```

Sesuaikan koneksi di kode jika perlu:
```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/blu_db";
private static final String DB_USER = "postgres";
private static final String DB_PASSWORD = "postgres";
```

## Cara Menjalankan

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BluBcaGrandSystem"
```
