package com.klinik.util;

// INTERFACE: kontrak untuk semua laporan yang bisa dicetak/diekspor
// Digunakan untuk menunjukkan POLYMORPHISM lewat interface
public interface Cetak {
    String buatRingkasan();
    void ekspor(String namaFile) throws java.io.IOException;
}
