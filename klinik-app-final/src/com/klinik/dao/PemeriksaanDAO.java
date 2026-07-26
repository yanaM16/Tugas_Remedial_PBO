package com.klinik.dao;

import com.klinik.model.*;
import com.klinik.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemeriksaanDAO {

    // Simpan pemeriksaan + detail obat dalam satu transaction (Connection.setAutoCommit)
    public void simpan(Pemeriksaan p) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlHeader = "INSERT INTO pemeriksaan (id_pemeriksaan, id_pasien, id_dokter, tanggal, keluhan, diagnosa, total_biaya) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlHeader)) {
                ps.setString(1, p.getIdPemeriksaan());
                ps.setString(2, p.getPasien().getId());
                ps.setString(3, p.getDokter().getId());
                ps.setString(4, p.getTanggal());
                ps.setString(5, p.getKeluhan());
                ps.setString(6, p.getDiagnosa());
                ps.setDouble(7, p.getTotalBiaya());
                ps.executeUpdate();
            }

            String sqlDetail = "INSERT INTO detail_pemeriksaan (id_pemeriksaan, kode_obat, jumlah, subtotal) VALUES (?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                for (DetailObat d : p.getDaftarObat()) {
                    ps.setString(1, p.getIdPemeriksaan());
                    ps.setString(2, d.getObat().getKodeObat());
                    ps.setInt(3, d.getJumlah());
                    ps.setDouble(4, d.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    // LAPORAN sederhana: daftar pemeriksaan pada rentang tanggal
    public List<Pemeriksaan> laporanByTanggal(String dariTanggal, String sampaiTanggal) throws SQLException {
        List<Pemeriksaan> hasil = new ArrayList<>();
        String sql = "SELECT pm.*, p.id as pid, p.no_rm, p.nama as pnama, p.alamat as palamat, p.no_telp as ptelp, " +
                "p.tanggal_lahir, p.jenis_kelamin, d.id as did, d.no_sip, d.nama as dnama, d.alamat as dalamat, " +
                "d.no_telp as dtelp, d.spesialisasi " +
                "FROM pemeriksaan pm " +
                "JOIN pasien p ON pm.id_pasien = p.id " +
                "JOIN dokter d ON pm.id_dokter = d.id " +
                "WHERE pm.tanggal BETWEEN ? AND ? ORDER BY pm.tanggal";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dariTanggal);
            ps.setString(2, sampaiTanggal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pasien pas = new Pasien(rs.getString("pid"), rs.getString("pnama"), rs.getString("palamat"),
                            rs.getString("ptelp"), rs.getString("no_rm"), rs.getString("tanggal_lahir"), rs.getString("jenis_kelamin"));
                    Dokter dok = new Dokter(rs.getString("did"), rs.getString("dnama"), rs.getString("dalamat"),
                            rs.getString("dtelp"), rs.getString("no_sip"), rs.getString("spesialisasi"));
                    Pemeriksaan pm = new Pemeriksaan(rs.getString("id_pemeriksaan"), pas, dok,
                            rs.getString("tanggal"), rs.getString("keluhan"), rs.getString("diagnosa"));
                    hasil.add(pm);
                }
            }
        }
        return hasil;
    }
}
