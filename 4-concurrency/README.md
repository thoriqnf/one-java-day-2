# BluBCA Concurrency Demo

Proyek Java JPMS — Demo **Concurrency** dengan ExecutorService Thread Pool.

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)

## 1. Kompilasi

```bash
mvn clean compile
```

## 2. Menjalankan

```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaConcurrencySystem"
```

## Struktur Proyek

```
4-concurrency/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── module-info.java
            └── com/
                └── Blubca/
                    └── finance/
                        └── BlueBcaConcurrencySystem.java
```

## Konsep yang Dibahas

| No | Konsep            | Deskripsi                                                           |
|----|-------------------|---------------------------------------------------------------------|
| 1  | ExecutorService   | Antarmuka untuk mengelola tugas paralel                             |
| 2  | Executors         | Kelas pembantu untuk membuat berbagai jenis Thread Pool             |
| 3  | newFixedThreadPool| 3 "pekerja" tetap — membatasi thread sesuai kapasitas server        |
| 4  | submit + Lambda   | Mengirim tugas ke pool menggunakan Lambda expression                |
| 5  | shutdown          | Menutup pool setelah semua tugas selesai dimasukkan                 |
| 6  | awaitTermination  | Menunggu maksimal 60 detik sampai semua tugas benar-benar selesai   |
| 7  | TimeUnit          | Mengatur durasi tunggu penutupan pool                               |
