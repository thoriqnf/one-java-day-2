package com.Blubca.finance.repository;

import com.Blubca.finance.domain.Transaksi;
import java.util.List;

/**
 * REPOSITORY INTERFACE
 * SOLID: Open/Closed Principle (OCP) & Dependency Inversion Principle (DIP).
 * Service layer akan bergantung pada interface ini, BUKAN pada implementasi SQL.
 */
public interface TransaksiRepository {

    /**
     * Mengambil semua transaksi dari data source.
     */
    List<Transaksi> findAll();
}
