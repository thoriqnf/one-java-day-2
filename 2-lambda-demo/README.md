# BluBCA Lambda Demo

Proyek Java JPMS — Demo **Lambda Expression**: Perbedaan Cara Lama vs Lambda.

## Prasyarat

- **Java 17** (JDK 17 atau lebih baru)
- **Apache Maven** (3.8+)

## 1. Kompilasi

```bash
mvn clean compile
```

## 2. Menjalankan

```bash
mvn exec:java -Dexec.mainClass="com.Blubca.finance.BlueBcaValidationSystem"
```

## Struktur Proyek

```
2-lambda-demo/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── module-info.java
            └── com/
                └── Blubca/
                    └── finance/
                        └── BlueBcaValidationSystem.java
```

## Konsep yang Dibahas

| No | Konsep             | Deskripsi                                                    |
|----|--------------------|--------------------------------------------------------------|
| 1  | Cara Lama          | Side-effect dengan variabel `totalPajak` yang bisa diubah    |
| 2  | Lambda             | `(usia) -> usia >= 17` — operator `->` sebagai jembatan      |
| 3  | Predicate          | `Predicate<Integer>` — fungsi yang menerima input dan return boolean |
| 4  | Runnable Lambda    | `() -> System.out.println(...)` — lambda tanpa parameter     |
| 5  | `.test()`          | Metode bawaan Predicate untuk menjalankan logika Lambda      |
