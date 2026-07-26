package com.klinik.model;

// INHERITANCE: Dokter mewarisi Person
public class Dokter extends Person {
    private String noSIP;        // Surat Izin Praktik
    private String spesialisasi;

    public Dokter(String id, String nama, String alamat, String noTelp,
                   String noSIP, String spesialisasi) {
        super(id, nama, alamat, noTelp);
        this.noSIP = noSIP;
        this.spesialisasi = spesialisasi;
    }

    public String getNoSIP() { return noSIP; }
    public void setNoSIP(String noSIP) { this.noSIP = noSIP; }

    public String getSpesialisasi() { return spesialisasi; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }

    // POLYMORPHISM: isi method berbeda dari Pasien walau nama method sama
    @Override
    public String getInfo() {
        return "Dokter " + getNama() + " (" + spesialisasi + ") - SIP: " + noSIP;
    }

    @Override
    public String getPeran() {
        return "Dokter";
    }
}
