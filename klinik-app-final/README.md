# Sistem Klinik Sederhana — Tugas Remedial PBO (Java)

## Struktur Project
```
klinik-app/
├── src/com/klinik/
│   ├── model/        -> Person(abstract), Pasien, Dokter, Obat, DetailObat, Pemeriksaan, User
│   ├── dao/           -> PasienDAO, DokterDAO, ObatDAO, UserDAO, PemeriksaanDAO (CRUD + search, JDBC)
│   ├── util/           -> DatabaseConnection, Cetak (interface), FileLogger, Validasi
│   ├── exception/     -> DataTidakDitemukanException, StokTidakCukupException
│   ├── gui/            -> LoginForm, MenuUtama, FormPasien, FormDokter, FormObat, FormPemeriksaan, FormLaporan
│   └── Main.java
├── database/klinik_db.sql
└── README.md
```

## Cara Menjalankan
1. Install MySQL, jalankan `database/klinik_db.sql` untuk membuat database `klinik_db` beserta data awal.
2. Sesuaikan username/password MySQL di `src/com/klinik/util/DatabaseConnection.java` jika perlu.
3. Download **MySQL Connector/J** (mysql-connector-j-8.x.x.jar) dari https://dev.mysql.com/downloads/connector/j/ dan masukkan ke classpath.
4. Compile:
   ```
   javac -d bin -cp mysql-connector-j-8.x.x.jar $(find src -name "*.java")
   ```
5. Jalankan:
   ```
   java -cp "bin;mysql-connector-j-8.x.x.jar" com.klinik.Main
   ```
   (Linux/Mac gunakan `:` bukan `;`)

## Login Default
- Username: `admin` / Password: `admin123`
- Username: `resepsionis` / Password: `resep123`

## Catatan
- Kode ini adalah kerangka fungsional lengkap (bukan pseudo-code) yang mengimplementasikan seluruh
  konsep wajib: Class & Object, Encapsulation, Inheritance, Polymorphism, Abstract Class, Interface,
  Package, Collection, String Method, Exception Handling, File Handling, dan Database (6 tabel, CRUD, pencarian).
- Silakan sesuaikan tampilan GUI, tambahkan validasi, dan uji coba end-to-end sebelum dikumpulkan.
- Wajib ambil screenshot aplikasi yang sudah berjalan untuk dilampirkan di Laporan Analisis Program.
