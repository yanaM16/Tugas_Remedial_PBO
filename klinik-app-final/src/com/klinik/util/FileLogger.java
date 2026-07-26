package com.klinik.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

// FILE HANDLING: mencatat log aktivitas/error aplikasi ke file teks
public class FileLogger {
    private static final String LOG_FILE = "klinik_log.txt";

    public static void log(String pesan) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write("[" + LocalDateTime.now() + "] " + pesan);
            bw.newLine();
        } catch (IOException e) {
            // Jika logging gagal, tampilkan di konsol agar tidak mengganggu aplikasi utama
            System.err.println("Gagal menulis log: " + e.getMessage());
        }
    }

    // Ekspor teks laporan ke file (dipakai fitur Laporan)
    public static void tulisKeFile(String namaFile, String isi) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(namaFile))) {
            bw.write(isi);
        }
    }
}
