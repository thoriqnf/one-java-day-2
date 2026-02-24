# BluBCA Digital Transaction Monitor

Proyek Java JPMS (Java Platform Module System) dengan konsep **Functional Interface, Lambda, dan Stream API**.

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)

## 1. Kompilasi

```bash
mvn clean compile
```

## 2. Menjalankan

```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BluBcaGrandSystem"
```

## 3. (Opsional) Setup Database PostgreSQL

Jika ingin menggunakan database:

```bash
psql -U postgres -c "CREATE DATABASE blu_db;"
psql -U postgres -d blu_db -f init.sql
```

## Struktur Proyek

```
1-TransactionMonitor/
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

| No | Konsep               | Deskripsi                                                       |
|----|----------------------|-----------------------------------------------------------------|
| 1  | Functional Interface | `BluTransactionRule` — kontrak aturan transaksi custom          |
| 2  | Lambda               | Implementasi `isHighValue` dengan lambda expression             |
| 3  | Stream API           | `filter`, `collect`, `forEach` untuk memproses transaksi        |
| 4  | Reduce               | `BigDecimal.ZERO` + `BigDecimal::add` untuk aggregation         |
| 5  | Method Reference     | `isHighValue::test`, `BigDecimal::add`                          |
| 6  | JPMS                 | `module-info.java` dengan `requires` dan `exports`              |
