-- Tabel transaksi untuk BlueBCA Cashback System
CREATE TABLE IF NOT EXISTS blu_cashback_trx (
  acc_no    VARCHAR(12),
  trx_amt   DECIMAL(15, 2),
  balance   DECIMAL(15, 2)
);

-- Data dummy: 5 transaksi nasabah
INSERT INTO blu_cashback_trx (acc_no, trx_amt, balance) VALUES
('880012345678', 50000.00,    2450000.00),
('880098765432', 200000.00,   5800000.00),
('880011223344', 1500000.00,  12300000.00),
('880055667788', 75000.00,    925000.00),
('880099887766', 300000.00,   3700000.00);
