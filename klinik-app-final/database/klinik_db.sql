-- ============================================
-- DATABASE: klinik_db
-- Sistem Klinik Sederhana - Tugas Remedial PBO
-- ============================================

CREATE DATABASE IF NOT EXISTS klinik_db;
USE klinik_db;

-- Tabel 1: users (untuk Login)
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Tabel 2: pasien
CREATE TABLE pasien (
    id VARCHAR(10) PRIMARY KEY,
    no_rm VARCHAR(20) UNIQUE NOT NULL,
    nama VARCHAR(100) NOT NULL,
    alamat VARCHAR(200),
    no_telp VARCHAR(15),
    tanggal_lahir VARCHAR(10),
    jenis_kelamin CHAR(1)
);

-- Tabel 3: dokter
CREATE TABLE dokter (
    id VARCHAR(10) PRIMARY KEY,
    no_sip VARCHAR(30) UNIQUE NOT NULL,
    nama VARCHAR(100) NOT NULL,
    alamat VARCHAR(200),
    no_telp VARCHAR(15),
    spesialisasi VARCHAR(50)
);

-- Tabel 4: obat
CREATE TABLE obat (
    kode_obat VARCHAR(10) PRIMARY KEY,
    nama_obat VARCHAR(100) NOT NULL,
    satuan VARCHAR(20),
    harga_satuan DOUBLE NOT NULL,
    stok INT NOT NULL DEFAULT 0
);

-- Tabel 5: pemeriksaan (transaksi/header)
CREATE TABLE pemeriksaan (
    id_pemeriksaan VARCHAR(15) PRIMARY KEY,
    id_pasien VARCHAR(10) NOT NULL,
    id_dokter VARCHAR(10) NOT NULL,
    tanggal VARCHAR(10) NOT NULL,
    keluhan VARCHAR(255),
    diagnosa VARCHAR(255),
    total_biaya DOUBLE DEFAULT 0,
    FOREIGN KEY (id_pasien) REFERENCES pasien(id),
    FOREIGN KEY (id_dokter) REFERENCES dokter(id)
);

-- Tabel 6: detail_pemeriksaan (detail resep obat per transaksi)
CREATE TABLE detail_pemeriksaan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pemeriksaan VARCHAR(15) NOT NULL,
    kode_obat VARCHAR(10) NOT NULL,
    jumlah INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (id_pemeriksaan) REFERENCES pemeriksaan(id_pemeriksaan),
    FOREIGN KEY (kode_obat) REFERENCES obat(kode_obat)
);

-- ============================================
-- DATA AWAL (SAMPLE DATA)
-- ============================================

INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('resepsionis', 'resep123', 'RESEPSIONIS');

INSERT INTO dokter (id, no_sip, nama, alamat, no_telp, spesialisasi) VALUES
('D001', 'SIP-001-2024', 'Andi Saputra', 'Jl. Merdeka No. 1', '081234567890', 'Umum'),
('D002', 'SIP-002-2024', 'Siti Rahayu', 'Jl. Sudirman No. 5', '081298765432', 'Anak');

INSERT INTO pasien (id, no_rm, nama, alamat, no_telp, tanggal_lahir, jenis_kelamin) VALUES
('P001', 'RM-0001', 'Budi Santoso', 'Jl. Kenanga No. 10', '081211112222', '1995-05-10', 'L'),
('P002', 'RM-0002', 'Dewi Lestari', 'Jl. Melati No. 3', '081233334444', '2000-08-21', 'P');

INSERT INTO obat (kode_obat, nama_obat, satuan, harga_satuan, stok) VALUES
('OB001', 'Paracetamol 500mg', 'Tablet', 500, 100),
('OB002', 'Amoxicillin 500mg', 'Kapsul', 1500, 50),
('OB003', 'Vitamin C', 'Tablet', 800, 80);
