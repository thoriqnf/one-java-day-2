# BluBCA Atomic Operations Demo

Proyek Java JPMS — Demo **Atomic Operations** dengan AtomicInteger untuk operasi thread-safe.

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)

## 1. Kompilasi

```bash
mvn clean compile
```

## 2. Menjalankan

```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaAtomicSystem"
```

## Struktur Proyek

```
5-atomic-operations/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── module-info.java
            └── com/
                └── Blubca/
                    └── finance/
                        └── BlueBcaAtomicSystem.java
```

## Konsep yang Dibahas

| No | Konsep           | Deskripsi                                                          |
|----|------------------|--------------------------------------------------------------------|
| 1  | AtomicInteger    | Operasi angka yang aman dari gangguan thread                       |
| 2  | incrementAndGet  | Operasi atomic: tambah 1 dan ambil hasilnya tanpa race condition   |
| 3  | Tanpa Lock Berat | Instruksi tingkat rendah di CPU, jauh lebih cepat dari synchronized|
| 4  | Akurasi Total    | Tidak terjadi double-counting pada penghitungan transaksi          |
| 5  | Thread Pool (10) | Simulasi 10 nasabah ambil antrean bersamaan                       |
