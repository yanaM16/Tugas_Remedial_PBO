package com.klinik.gui;

import com.klinik.dao.PemeriksaanDAO;
import com.klinik.model.Pemeriksaan;
import com.klinik.util.Cetak;
import com.klinik.util.FileLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// LAPORAN SEDERHANA: menampilkan & mengekspor daftar pemeriksaan pada rentang tanggal
// Mengimplementasikan interface Cetak -> contoh POLYMORPHISM lewat interface
public class FormLaporan extends JFrame implements Cetak {
    private JTextField txtDari, txtSampai;
    private JTable table;
    private DefaultTableModel model;
    private PemeriksaanDAO dao = new PemeriksaanDAO();
    private List<Pemeriksaan> dataLaporan;

    public FormLaporan() {
        setTitle("Laporan Kunjungan Pemeriksaan");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel filter = new JPanel();
        txtDari = new JTextField(10);
        txtSampai = new JTextField(10);
        JButton btnTampil = new JButton("Tampilkan");
        JButton btnEkspor = new JButton("Ekspor ke File .txt");
        filter.add(new JLabel("Dari (yyyy-mm-dd):")); filter.add(txtDari);
        filter.add(new JLabel("Sampai:")); filter.add(txtSampai);
        filter.add(btnTampil);
        filter.add(btnEkspor);

        model = new DefaultTableModel(new String[]{"ID", "Tanggal", "Pasien", "Dokter", "Diagnosa", "Total Biaya"}, 0);
        table = new JTable(model);

        add(filter, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnTampil.addActionListener(e -> tampilkanLaporan());
        btnEkspor.addActionListener(e -> eksporLaporan());
    }

    private void tampilkanLaporan() {
        try {
            dataLaporan = dao.laporanByTanggal(txtDari.getText(), txtSampai.getText());
            model.setRowCount(0);
            for (Pemeriksaan p : dataLaporan) {
                model.addRow(new Object[]{p.getIdPemeriksaan(), p.getTanggal(), p.getPasien().getNama(),
                        p.getDokter().getNama(), p.getDiagnosa(), p.getTotalBiaya()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan: " + ex.getMessage());
        }
    }

    private void eksporLaporan() {
        try {
            ekspor("laporan_kunjungan.txt");
            JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor ke file laporan_kunjungan.txt");
        } catch (IOException ex) {
            FileLogger.log("ERROR ekspor laporan: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Gagal mengekspor laporan: " + ex.getMessage());
        }
    }

    // Implementasi method dari interface Cetak
    @Override
    public String buatRingkasan() {
        StringBuilder sb = new StringBuilder();
        sb.append("LAPORAN KUNJUNGAN KLINIK\n");
        sb.append("Periode: ").append(txtDari.getText()).append(" s/d ").append(txtSampai.getText()).append("\n\n");
        double totalKeseluruhan = 0;
        if (dataLaporan != null) {
            for (Pemeriksaan p : dataLaporan) {
                sb.append(p.getIdPemeriksaan()).append(" | ").append(p.getTanggal()).append(" | ")
                  .append(p.getPasien().getNama()).append(" | ").append(p.getDokter().getNama())
                  .append(" | ").append(p.getDiagnosa()).append(" | Rp ").append(p.getTotalBiaya()).append("\n");
                totalKeseluruhan += p.getTotalBiaya();
            }
        }
        sb.append("\nTOTAL PENDAPATAN: Rp ").append(totalKeseluruhan);
        return sb.toString();
    }

    @Override
    public void ekspor(String namaFile) throws IOException {
        FileLogger.tulisKeFile(namaFile, buatRingkasan());
    }
}
