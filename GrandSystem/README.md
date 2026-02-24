# BluBCA Grand System

Proyek Java JPMS (Java Platform Module System) dengan integrasi PostgreSQL.  
Menggabungkan konsep: **Functional, Lambda, Stream, Concurrency, Atomic, dan Parallel**.

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)
- **PostgreSQL** (versi 12+)

## 1. Setup Database (PostgreSQL)

Buat database terlebih dahulu, lalu jalankan script SQL:

```bash
# Buat database
psql -U postgres -c "CREATE DATABASE blu_db;"

# Jalankan init.sql untuk membuat tabel dan memasukkan data
psql -U postgres -d blu_db -f init.sql
```

Script `init.sql` akan:
- Membuat tabel `Blu_transactions` dengan kolom: `id`, `customer_name`, `amount`, `status`
- Memasukkan 5 data contoh

## 2. Konfigurasi Koneksi Database

Sesuaikan konfigurasi koneksi di `BluBcaGrandSystem.java` jika diperlukan:

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/blu_db";
private static final String DB_USER = "postgres";
private static final String DB_PASSWORD = "postgres";
```

## 3. Kompilasi

```bash
mvn clean compile
```

## 4. Menjalankan

```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BluBcaGrandSystem"
```

## Struktur Proyek

```
BluBcaProject/
├── pom.xml
├── init.sql
├── README.md
└── src/
    └── main/
        └── java/
            ├── module-info.java
            └── com/
                └── Blubca/
                    └── finance/
                        └── BluBcaGrandSystem.java
```

## Konsep yang Dibahas

| No | Konsep               | Deskripsi                                                     |
|----|----------------------|---------------------------------------------------------------|
| 1  | Lambda               | Predicate, Function, Consumer, Supplier                       |
| 2  | Functional Interface | Menggunakan built-in functional interfaces dari `java.util.function` |
| 3  | Stream API           | filter, map, collect, groupingBy, reduce                      |
| 4  | Parallel Stream      | Perbandingan sequential vs parallel stream                    |
| 5  | Concurrency          | ExecutorService, Future, Callable                             |
| 6  | Atomic               | AtomicInteger, AtomicLong, AtomicReference                    |
| 7  | JPMS                 | module-info.java, requires, exports                           |
| 8  | PostgreSQL           | JDBC, PreparedStatement, ResultSet                            |
