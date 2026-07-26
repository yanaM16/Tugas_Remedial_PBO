package com.klinik.model;

// ABSTRACT CLASS -> menjadi induk (superclass) untuk Pasien dan Dokter
// Menunjukkan konsep INHERITANCE dan ABSTRACTION
public abstract class Person {
    // ENCAPSULATION: field private, hanya bisa diakses lewat getter/setter
    private String id;
    private String nama;
    private String alamat;
    private String noTelp;

    public Person(String id, String nama, String alamat, String noTelp) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.noTelp = noTelp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) {
        // STRING METHOD: memastikan nama tersimpan rapi (kapital di awal kata)
        if (nama != null && !nama.trim().isEmpty()) {
            String[] kata = nama.trim().toLowerCase().split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String k : kata) {
                sb.append(Character.toUpperCase(k.charAt(0))).append(k.substring(1)).append(" ");
            }
            this.nama = sb.toString().trim();
        } else {
            this.nama = nama;
        }
    }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    // METHOD ABSTRACT -> wajib di-override oleh subclass (dasar POLYMORPHISM)
    public abstract String getInfo();

    // METHOD ABSTRACT kedua untuk membedakan peran tiap Person
    public abstract String getPeran();
}
