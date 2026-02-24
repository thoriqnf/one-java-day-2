# BluBCA Stream API Demo

Proyek Java JPMS — Demo **Stream API Pipeline**: Filtering → Mapping → Aggregation (Reduce).

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)

## 1. Kompilasi

```bash
mvn clean compile
```

## 2. Menjalankan

```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaCashbackSystem"
```

## Struktur Proyek

```
3-stream-api/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── module-info.java
            └── com/
                └── Blubca/
                    └── finance/
                        └── BlueBcaCashbackSystem.java
```

## Konsep yang Dibahas

| No | Konsep      | Deskripsi                                                        |
|----|-------------|------------------------------------------------------------------|
| 1  | Stream      | `transactions.stream()` — mengubah List menjadi aliran data      |
| 2  | Filter      | `.filter(amt -> amt.compareTo(...) > 0)` — ambil transaksi > 100K |
| 3  | Map         | `.map(amt -> amt.multiply(...))` — hitung cashback 5%            |
| 4  | Reduce      | `.reduce(BigDecimal.ZERO, BigDecimal::add)` — jumlahkan total    |
| 5  | BigDecimal  | Akurasi nilai uang di perbankan                                  |
| 6  | RoundingMode| `.setScale(2, RoundingMode.HALF_UP)` — pembulatan 2 desimal      |
