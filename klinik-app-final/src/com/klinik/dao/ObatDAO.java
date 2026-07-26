package com.klinik.dao;

import com.klinik.model.Obat;
import com.klinik.exception.DataTidakDitemukanException;
import com.klinik.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObatDAO {

    public void tambah(Obat o) throws SQLException {
        String sql = "INSERT INTO obat (kode_obat, nama_obat, satuan, harga_satuan, stok) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getKodeObat());
            ps.setString(2, o.getNamaObat());
            ps.setString(3, o.getSatuan());
            ps.setDouble(4, o.getHargaSatuan());
            ps.setInt(5, o.getStok());
            ps.executeUpdate();
        }
    }

    public void ubah(Obat o) throws SQLException {
        String sql = "UPDATE obat SET nama_obat=?, satuan=?, harga_satuan=?, stok=? WHERE kode_obat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getNamaObat());
            ps.setString(2, o.getSatuan());
            ps.setDouble(3, o.getHargaSatuan());
            ps.setInt(4, o.getStok());
            ps.setString(5, o.getKodeObat());
            ps.executeUpdate();
        }
    }

    public void hapus(String kodeObat) throws SQLException {
        String sql = "DELETE FROM obat WHERE kode_obat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodeObat);
            ps.executeUpdate();
        }
    }

    public List<Obat> cari(String kataKunci) throws SQLException {
        List<Obat> hasil = new ArrayList<>();
        String sql = "SELECT * FROM obat WHERE nama_obat LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kataKunci + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) hasil.add(mapRow(rs));
            }
        }
        return hasil;
    }

    public List<Obat> semua() throws SQLException {
        List<Obat> hasil = new ArrayList<>();
        String sql = "SELECT * FROM obat ORDER BY nama_obat";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) hasil.add(mapRow(rs));
        }
        return hasil;
    }

    public Obat cariByKode(String kode) throws SQLException, DataTidakDitemukanException {
        String sql = "SELECT * FROM obat WHERE kode_obat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                throw new DataTidakDitemukanException("Obat dengan kode " + kode + " tidak ditemukan.");
            }
        }
    }

    // Update stok setelah dipakai dalam pemeriksaan
    public void updateStok(String kodeObat, int stokBaru) throws SQLException {
        String sql = "UPDATE obat SET stok=? WHERE kode_obat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stokBaru);
            ps.setString(2, kodeObat);
            ps.executeUpdate();
        }
    }

    private Obat mapRow(ResultSet rs) throws SQLException {
        return new Obat(rs.getString("kode_obat"), rs.getString("nama_obat"),
            rs.getString("satuan"), rs.getDouble("harga_satuan"), rs.getInt("stok"));
    }
}
