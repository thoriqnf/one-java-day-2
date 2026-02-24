package com.Blubca.finance.repository;

import com.Blubca.finance.domain.Transaksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITORY IMPLEMENTATION
 * Menangani spesifik ke PostgreSQL.
 * Mengimplementasikan abstract contract dari TransaksiRepository.
 */
public class PostgresTransaksiRepository implements TransaksiRepository {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    @Override
    public List<Transaksi> findAll() {
        List<Transaksi> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            buatTabelJikaBelumAda(conn);

            String sql = "SELECT id, customer_name, amount, status FROM Blu_transactions";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(new Transaksi(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                    ));
                }
            }
            System.out.println("Berhasil terhubung ke PostgreSQL lewat Repository Layer.\n");

        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
        }
        return list;
    }

    private void buatTabelJikaBelumAda(Connection conn) throws SQLException {
        String createTable = """
            CREATE TABLE IF NOT EXISTS Blu_transactions (
              id SERIAL PRIMARY KEY,
              customer_name VARCHAR(100),
              amount DECIMAL(15, 2),
              status VARCHAR(20)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        }

        String countSql = "SELECT COUNT(*) FROM Blu_transactions";
        try (PreparedStatement stmt = conn.prepareStatement(countSql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            if (rs.getInt(1) == 0) {
                String seed = """
                    INSERT INTO Blu_transactions (customer_name, amount, status) VALUES
                    ('Budi Utomo', 500000.00, 'SUCCESS'),
                    ('Siti Aminah', 15000000.00, 'SUCCESS'),
                    ('Andi Wijaya', 250000.00, 'PENDING'),
                    ('Rina Lestari', 125000000.00, 'SUCCESS'),
                    ('Dewi Sartika', 750000.00, 'SUCCESS')
                    """;
                try (Statement seedStmt = conn.createStatement()) {
                    seedStmt.execute(seed);
                    System.out.println("Tabel Blu_transactions dibuat dan diisi 5 data dummy.");
                }
            }
        }
    }
}
