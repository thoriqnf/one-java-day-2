# BluBCA Parallel Processing Demo

Proyek Java JPMS — Demo **Parallel Stream** untuk pemrosesan data skala besar.

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)

## 1. Kompilasi

```bash
mvn clean compile
```

## 2. Menjalankan

**Versi Sederhana** (BlueBcaParallelSystem):
```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaParallelSystem"
```

**Versi Terstruktur** (BlueBcaAuditSystem):
```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaAuditSystem"
```

## Struktur Proyek

```
6-parallel-processing/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── module-info.java
            └── com/
                └── Blubca/
                    └── finance/
                        ├── BlueBcaParallelSystem.java
                        └── BlueBcaAuditSystem.java
```

## Konsep yang Dibahas

| No | Konsep                   | Deskripsi                                                       |
|----|--------------------------|-----------------------------------------------------------------|
| 1  | LongStream.rangeClosed   | Membuat stream angka dari 1 sampai 10 juta                     |
| 2  | .parallel()              | Mengaktifkan pemrosesan paralel di semua core CPU               |
| 3  | Separation of Concerns   | Logika bisnis dipisahkan dari main (performParallelAudit)       |
| 4  | Instance Method          | `new BlueBcaAuditSystem()` — membuat objek untuk memanggil fungsi|
| 5  | Benchmark Waktu          | `System.currentTimeMillis()` untuk mengukur waktu eksekusi      |
