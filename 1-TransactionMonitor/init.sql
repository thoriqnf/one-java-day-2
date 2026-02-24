-- Membuat tabel transaksi untuk BluBCA
CREATE TABLE Blu_transactions (
  id SERIAL PRIMARY KEY,
  customer_name VARCHAR(100),
  amount DECIMAL(15, 2),
  status VARCHAR(20)
);

-- Memasukkan dummy data (100.000 data bisa digenerate, ini contoh 5 pertama)
INSERT INTO Blu_transactions (customer_name, amount, status) VALUES
('Budi Utomo', 500000.00, 'SUCCESS'),
('Siti Aminah', 15000000.00, 'SUCCESS'),
('Andi Wijaya', 250000.00, 'PENDING'),
('Rina Lestari', 125000000.00, 'SUCCESS'),
('Dewi Sartika', 750000.00, 'SUCCESS');
