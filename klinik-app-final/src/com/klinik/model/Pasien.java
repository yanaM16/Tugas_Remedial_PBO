package com.klinik.model;

// INHERITANCE: Pasien mewarisi Person
public class Pasien extends Person {
    private String noRM;          // Nomor Rekam Medis
    private String tanggalLahir;  // format yyyy-MM-dd
    private String jenisKelamin;  // L / P

    public Pasien(String id, String nama, String alamat, String noTelp,
                   String noRM, String tanggalLahir, String jenisKelamin) {
        super(id, nama, alamat, noTelp); // memanggil constructor superclass
        this.noRM = noRM;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
    }

    public String getNoRM() { return noRM; }
    public void setNoRM(String noRM) { this.noRM = noRM; }

    public String getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    // POLYMORPHISM: implementasi berbeda dari method abstract di Person
    @Override
    public String getInfo() {
        return "Pasien [" + getNoRM() + "] " + getNama() + " - " + jenisKelamin + ", Lahir: " + tanggalLahir;
    }

    @Override
    public String getPeran() {
        return "Pasien";
    }
}
