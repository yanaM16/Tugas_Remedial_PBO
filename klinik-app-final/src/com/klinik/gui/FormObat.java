package com.klinik.gui;

import com.klinik.dao.ObatDAO;
import com.klinik.model.Obat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FormObat extends JFrame {
    private JTextField txtKode, txtNama, txtSatuan, txtHarga, txtStok, txtCari;
    private JTable table;
    private DefaultTableModel model;
    private ObatDAO dao = new ObatDAO();

    public FormObat() {
        setTitle("Data Master Obat");
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(3, 4, 5, 5));
        txtKode = new JTextField(); txtNama = new JTextField();
        txtSatuan = new JTextField(); txtHarga = new JTextField(); txtStok = new JTextField();
        form.add(new JLabel("Kode Obat:")); form.add(txtKode);
        form.add(new JLabel("Nama Obat:")); form.add(txtNama);
        form.add(new JLabel("Satuan:")); form.add(txtSatuan);
        form.add(new JLabel("Harga Satuan:")); form.add(txtHarga);
        form.add(new JLabel("Stok:")); form.add(txtStok);

        JPanel tombol = new JPanel();
        JButton btnTambah = new JButton("Tambah"), btnUbah = new JButton("Ubah"),
                btnHapus = new JButton("Hapus"), btnBersih = new JButton("Bersihkan");
        tombol.add(btnTambah); tombol.add(btnUbah); tombol.add(btnHapus); tombol.add(btnBersih);

        JPanel cariPanel = new JPanel(new BorderLayout());
        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari");
        cariPanel.add(new JLabel("Cari nama obat: "), BorderLayout.WEST);
        cariPanel.add(txtCari, BorderLayout.CENTER);
        cariPanel.add(btnCari, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"Kode", "Nama", "Satuan", "Harga", "Stok"}, 0);
        table = new JTable(model);

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(tombol, BorderLayout.SOUTH);
        JPanel utara = new JPanel(new BorderLayout());
        utara.add(atas, BorderLayout.NORTH);
        utara.add(cariPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(utara, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambah());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersih());
        btnCari.addActionListener(e -> cari());
        table.getSelectionModel().addListSelectionListener(e -> isiForm());

        muat();
    }

    private void tambah() {
        try {
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            dao.tambah(new Obat(txtKode.getText(), txtNama.getText(), txtSatuan.getText(), harga, stok));
            JOptionPane.showMessageDialog(this, "Data obat ditambahkan.");
            muat(); bersih();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void ubah() {
        try {
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            dao.ubah(new Obat(txtKode.getText(), txtNama.getText(), txtSatuan.getText(), harga, stok));
            JOptionPane.showMessageDialog(this, "Data obat diubah.");
            muat();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void hapus() {
        try {
            dao.hapus(txtKode.getText());
            muat(); bersih();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void cari() {
        try {
            tampilkan(dao.cari(txtCari.getText()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void muat() {
        try {
            tampilkan(dao.semua());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage());
        }
    }

    private void tampilkan(List<Obat> daftar) {
        model.setRowCount(0);
        for (Obat o : daftar) {
            model.addRow(new Object[]{o.getKodeObat(), o.getNamaObat(), o.getSatuan(), o.getHargaSatuan(), o.getStok()});
        }
    }

    private void isiForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtKode.setText(model.getValueAt(row, 0).toString());
        txtNama.setText(model.getValueAt(row, 1).toString());
        txtSatuan.setText(model.getValueAt(row, 2).toString());
        txtHarga.setText(model.getValueAt(row, 3).toString());
        txtStok.setText(model.getValueAt(row, 4).toString());
    }

    private void bersih() {
        txtKode.setText(""); txtNama.setText(""); txtSatuan.setText("");
        txtHarga.setText(""); txtStok.setText("");
    }
}
