/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Koneksi.KoneksiDB;
import Model.ModelTransaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Felda Muffarihati
 */
public class ControllerDashboard {

    private ControllerKeuangan control = new ControllerKeuangan();

    public double getTotalPemasukan() {

        double total = 0;

        ArrayList<ModelTransaksi> data =
                control.ambilSemuaModelTransaksi();

        for (ModelTransaksi t : data) {
            if (t.getJenis().equalsIgnoreCase("Pemasukan")) {
                total += t.getNominal();
            }
        }

        return total;
    }

    public double getTotalPengeluaran() {

        double total = 0;

        ArrayList<ModelTransaksi> data =
                control.ambilSemuaModelTransaksi();

        for (ModelTransaksi t : data) {
            if (t.getJenis().equalsIgnoreCase("Pengeluaran")) {
                total += t.getNominal();
            }
        }

        return total;
    }

    public double getTotalSaldo() {
        return getTotalPemasukan() - getTotalPengeluaran();
    }

    public Object[][] getDataDashboard() {

    ArrayList<Object[]> list = new ArrayList<>();
    try {
        Connection conn = KoneksiDB.getKoneksi();
        String sql = "SELECT tanggal, jenis, kategori, nominal, keterangan FROM transaksi";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Object[] row = {
                rs.getString("tanggal"),
                rs.getString("jenis"),
                rs.getString("kategori"),
                rs.getDouble("nominal"),
                rs.getString("keterangan")
            };
            list.add(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list.toArray(new Object[0][0]);
}
}