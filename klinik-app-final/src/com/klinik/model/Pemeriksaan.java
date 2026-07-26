package com.klinik.model;

import java.util.ArrayList;
import java.util.List;

// Merepresentasikan transaksi pemeriksaan pasien
public class Pemeriksaan {
    private String idPemeriksaan;
    private Pasien pasien;
    private Dokter dokter;
    private String tanggal; // yyyy-MM-dd
    private String keluhan;
    private String diagnosa;
    // COLLECTION: menyimpan daftar obat yang diresepkan
    private List<DetailObat> daftarObat = new ArrayList<>();

    public Pemeriksaan(String idPemeriksaan, Pasien pasien, Dokter dokter,
                        String tanggal, String keluhan, String diagnosa) {
        this.idPemeriksaan = idPemeriksaan;
        this.pasien = pasien;
        this.dokter = dokter;
        this.tanggal = tanggal;
        this.keluhan = keluhan;
        this.diagnosa = diagnosa;
    }

    public String getIdPemeriksaan() { return idPemeriksaan; }
    public void setIdPemeriksaan(String idPemeriksaan) { this.idPemeriksaan = idPemeriksaan; }
    public Pasien getPasien() { return pasien; }
    public void setPasien(Pasien pasien) { this.pasien = pasien; }
    public Dokter getDokter() { return dokter; }
    public void setDokter(Dokter dokter) { this.dokter = dokter; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getKeluhan() { return keluhan; }
    public void setKeluhan(String keluhan) { this.keluhan = keluhan; }
    public String getDiagnosa() { return diagnosa; }
    public void setDiagnosa(String diagnosa) { this.diagnosa = diagnosa; }
    public List<DetailObat> getDaftarObat() { return daftarObat; }

    public void tambahObat(DetailObat d) { daftarObat.add(d); }

    public double getTotalBiaya() {
        double total = 0;
        for (DetailObat d : daftarObat) {
            total += d.getSubtotal();
        }
        return total;
    }
}
