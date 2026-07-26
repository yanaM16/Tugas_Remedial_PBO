package com.klinik.model;

public class Obat {
    private String kodeObat;
    private String namaObat;
    private String satuan;
    private double hargaSatuan;
    private int stok;

    public Obat(String kodeObat, String namaObat, String satuan, double hargaSatuan, int stok) {
        this.kodeObat = kodeObat;
        this.namaObat = namaObat;
        this.satuan = satuan;
        this.hargaSatuan = hargaSatuan;
        this.stok = stok;
    }

    public String getKodeObat() { return kodeObat; }
    public void setKodeObat(String kodeObat) { this.kodeObat = kodeObat; }
    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public void kurangiStok(int jumlah) throws com.klinik.exception.StokTidakCukupException {
        if (jumlah > stok) {
            throw new com.klinik.exception.StokTidakCukupException(
                "Stok " + namaObat + " tidak cukup. Sisa stok: " + stok);
        }
        this.stok -= jumlah;
    }
}
