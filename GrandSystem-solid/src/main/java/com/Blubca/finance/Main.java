package com.Blubca.finance;

import com.Blubca.finance.repository.PostgresTransaksiRepository;
import com.Blubca.finance.repository.TransaksiRepository;
import com.Blubca.finance.service.TransaksiService;

/**
 * ENTRY POINT & DEPENDENCY INJECTION WIRING
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   SELAMAT DATANG DI BluBCA GRAND SYSTEM");
        System.out.println("   Memakai Clean Architecture + SOLID");
        System.out.println("==============================================\n");

        // Dependency Wiring (Manual DI)
        // 1. Instansiasi concrete repository (PostgreSQL)
        TransaksiRepository respository = new PostgresTransaksiRepository();

        // 2. Inject repository ke Service
        TransaksiService service = new TransaksiService(respository);

        // 3. Jalankan aplikasi via Service layer
        service.jalankanSemuaDemo();

        System.out.println("\n==============================================");
        System.out.println("   SEMUA DEMO SELESAI!");
        System.out.println("==============================================");
    }
}
