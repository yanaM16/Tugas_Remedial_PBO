package com.klinik.model;

// Item obat yang dipakai dalam satu pemeriksaan
public class DetailObat {
    private Obat obat;
    private int jumlah;

    public DetailObat(Obat obat, int jumlah) {
        this.obat = obat;
        this.jumlah = jumlah;
    }

    public Obat getObat() { return obat; }
    public void setObat(Obat obat) { this.obat = obat; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getSubtotal() { return obat.getHargaSatuan() * jumlah; }
}
