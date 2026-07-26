package com.klinik.util;

// Kumpulan validasi berbasis STRING METHOD (equals, matches, trim, isEmpty, length)
public class Validasi {
    public static boolean isKosong(String teks) {
        return teks == null || teks.trim().isEmpty();
    }

    public static boolean isNoTelpValid(String noTelp) {
        // hanya angka, panjang 9-13 digit
        return noTelp != null && noTelp.matches("\\d{9,13}");
    }

    public static boolean isTanggalValid(String tanggal) {
        return tanggal != null && tanggal.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
