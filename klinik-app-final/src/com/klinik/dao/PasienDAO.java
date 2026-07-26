package com.klinik.dao;

import com.klinik.model.Pasien;
import com.klinik.exception.DataTidakDitemukanException;
import com.klinik.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasienDAO {

    public void tambah(Pasien p) throws SQLException {
        String sql = "INSERT INTO pasien (id, no_rm, nama, alamat, no_telp, tanggal_lahir, jenis_kelamin) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNoRM());
            ps.setString(3, p.getNama());
            ps.setString(4, p.getAlamat());
            ps.setString(5, p.getNoTelp());
            ps.setString(6, p.getTanggalLahir());
            ps.setString(7, p.getJenisKelamin());
            ps.executeUpdate();
        }
    }

    public void ubah(Pasien p) throws SQLException {
        String sql = "UPDATE pasien SET no_rm=?, nama=?, alamat=?, no_telp=?, tanggal_lahir=?, jenis_kelamin=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNoRM());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getAlamat());
            ps.setString(4, p.getNoTelp());
            ps.setString(5, p.getTanggalLahir());
            ps.setString(6, p.getJenisKelamin());
            ps.setString(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void hapus(String id) throws SQLException {
        String sql = "DELETE FROM pasien WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Pasien cariById(String id) throws SQLException, DataTidakDitemukanException {
        String sql = "SELECT * FROM pasien WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                throw new DataTidakDitemukanException("Pasien dengan ID " + id + " tidak ditemukan.");
            }
        }
    }

    // PENCARIAN DATA (nama atau no rekam medis)
    public List<Pasien> cari(String kataKunci) throws SQLException {
        List<Pasien> hasil = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE nama LIKE ? OR no_rm LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kataKunci + "%");
            ps.setString(2, "%" + kataKunci + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) hasil.add(mapRow(rs));
            }
        }
        return hasil;
    }

    public List<Pasien> semua() throws SQLException {
        List<Pasien> hasil = new ArrayList<>();
        String sql = "SELECT * FROM pasien ORDER BY nama";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) hasil.add(mapRow(rs));
        }
        return hasil;
    }

    private Pasien mapRow(ResultSet rs) throws SQLException {
        return new Pasien(
            rs.getString("id"), rs.getString("nama"), rs.getString("alamat"),
            rs.getString("no_telp"), rs.getString("no_rm"),
            rs.getString("tanggal_lahir"), rs.getString("jenis_kelamin")
        );
    }
}
