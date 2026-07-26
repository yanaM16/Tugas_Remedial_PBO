package com.klinik.dao;

import com.klinik.model.Dokter;
import com.klinik.exception.DataTidakDitemukanException;
import com.klinik.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {

    public void tambah(Dokter d) throws SQLException {
        String sql = "INSERT INTO dokter (id, no_sip, nama, alamat, no_telp, spesialisasi) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getId());
            ps.setString(2, d.getNoSIP());
            ps.setString(3, d.getNama());
            ps.setString(4, d.getAlamat());
            ps.setString(5, d.getNoTelp());
            ps.setString(6, d.getSpesialisasi());
            ps.executeUpdate();
        }
    }

    public void ubah(Dokter d) throws SQLException {
        String sql = "UPDATE dokter SET no_sip=?, nama=?, alamat=?, no_telp=?, spesialisasi=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNoSIP());
            ps.setString(2, d.getNama());
            ps.setString(3, d.getAlamat());
            ps.setString(4, d.getNoTelp());
            ps.setString(5, d.getSpesialisasi());
            ps.setString(6, d.getId());
            ps.executeUpdate();
        }
    }

    public void hapus(String id) throws SQLException {
        String sql = "DELETE FROM dokter WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public List<Dokter> cari(String kataKunci) throws SQLException {
        List<Dokter> hasil = new ArrayList<>();
        String sql = "SELECT * FROM dokter WHERE nama LIKE ? OR spesialisasi LIKE ?";
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

    public List<Dokter> semua() throws SQLException {
        List<Dokter> hasil = new ArrayList<>();
        String sql = "SELECT * FROM dokter ORDER BY nama";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) hasil.add(mapRow(rs));
        }
        return hasil;
    }

    public Dokter cariById(String id) throws SQLException, DataTidakDitemukanException {
        String sql = "SELECT * FROM dokter WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                throw new DataTidakDitemukanException("Dokter dengan ID " + id + " tidak ditemukan.");
            }
        }
    }

    private Dokter mapRow(ResultSet rs) throws SQLException {
        return new Dokter(
            rs.getString("id"), rs.getString("nama"), rs.getString("alamat"),
            rs.getString("no_telp"), rs.getString("no_sip"), rs.getString("spesialisasi")
        );
    }
}
