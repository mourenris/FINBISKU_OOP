package Controller;

import Model.ModelRekapKategori;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Koneksi.KoneksiDB;
import Model.LaporanKeuangan;
import Model.ModelCashflow;
import Model.ModelTransaksi;
import java.util.ArrayList;

/**
 *
 * @author Felda Muffarihati
 */
public class ControllerCashflow {

    private LaporanKeuangan laporan;
    private ControllerKeuangan control = new ControllerKeuangan();

    public ControllerCashflow() {
        this.laporan = new LaporanKeuangan("Semua Periode");
    }

    public ControllerCashflow(String periode) {
        this.laporan = new LaporanKeuangan(periode);
    }

    public LaporanKeuangan getLaporan() {
        return laporan;
    }

    public boolean simpanTransaksi(
            String tgl,
            String jenis,
            String kat,
            double nom,
            String ket) {

        ModelTransaksi t = new ModelTransaksi();
        t.setIdTransaksi(laporan.getDaftarTransaksi().size() + 1);
        t.setIdUser(1);
        t.setTanggal(tgl);
        t.setJenis(jenis);
        t.setKategori(kat);
        t.setNominal(nom);
        t.setKeterangan(ket);
        laporan.tambahTransaksi(t);

        return control.simpanTransaksi(tgl, jenis, kat, nom, ket);
    }

    public boolean ubahTransaksi(
            int idTerpilih,
            String jenis,
            String kat,
            double nom,
            String ket) {

        for (ModelTransaksi t : laporan.getDaftarTransaksi()) {
            if (t.getIdTransaksi() == idTerpilih) {
                t.setJenis(jenis);
                t.setKategori(kat);
                t.setNominal(nom);
                t.setKeterangan(ket);
                break;
            }
        }
        return control.ubahTransaksi(idTerpilih, jenis, kat, nom, ket);
    }

    public boolean hapusTransaksi(int idTerpilih) {
        ModelTransaksi toRemove = null;
        for (ModelTransaksi t : laporan.getDaftarTransaksi()) {
            if (t.getIdTransaksi() == idTerpilih) {
                toRemove = t;
                break;
            }
        }
        if (toRemove != null) {
            laporan.hapusTransaksi(toRemove);
        }
        return control.hapusTransaksi(idTerpilih);
    }

    public ArrayList<ModelTransaksi> ambilSemuaModelTransaksi() {
        return control.ambilSemuaModelTransaksi();
    }

    public void tambahCashflow(ModelCashflow cashflow) {
        laporan.tambahCashflow(cashflow);
    }

    public double getTotalPengeluaran(String bulan, int tahun) {
        double total = 0;
        ArrayList<ModelTransaksi> data = control.ambilSemuaModelTransaksi();

        for (ModelTransaksi t : data) {
            String[] bagianTgl = t.getTanggal().split("-");
            int tahunData = Integer.parseInt(bagianTgl[0]);
            int bulanData = Integer.parseInt(bagianTgl[1]);

            if (t.getJenis().equalsIgnoreCase("Pengeluaran")
                    && bulanData == convertBulan(bulan)
                    && tahunData == tahun) {
                total += t.getNominal();
            }
        }
        return total;
    }

    public double getTotalPemasukan(String bulan, int tahun) {
        double total = 0;
        ArrayList<ModelTransaksi> data = control.ambilSemuaModelTransaksi();

        for (ModelTransaksi t : data) {
            String[] bagianTgl = t.getTanggal().split("-");
            int tahunData = Integer.parseInt(bagianTgl[0]);
            int bulanData = Integer.parseInt(bagianTgl[1]);

            if (t.getJenis().equalsIgnoreCase("Pemasukan")
                    && bulanData == convertBulan(bulan)
                    && tahunData == tahun) {
                total += t.getNominal();
            }
        }
        return total;
    }

public ArrayList<ModelRekapKategori> getRekapPerKategori(String bulan, int tahun) {
    ArrayList<ModelRekapKategori> list = new ArrayList<>();
    Connection conn = null;

    int nomorBulan = convertBulan(bulan);
    if (nomorBulan == 0) return list;

    String periodeFilter = String.format("%04d-%02d", tahun, nomorBulan);

    String sql =
        "SELECT kategori, jenis, SUM(nominal) AS total " +
        "FROM transaksi " +
        "WHERE DATE_FORMAT(tanggal, '%Y-%m') = ? " +
        "GROUP BY kategori, jenis " +
        "ORDER BY jenis DESC, kategori ASC";

    try {
        conn = KoneksiDB.getKoneksi();
        if (conn == null) return list;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, periodeFilter);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new ModelRekapKategori(
                rs.getString("kategori"),
                rs.getString("jenis"),
                rs.getDouble("total")
            ));
        }
        ps.close();

    } catch (Exception e) {
        System.out.println("[RekapKategori] ERROR: " + e.getMessage());
    } finally {
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }

    return list;
}
    
    private int convertBulan(String bulan) {
        switch (bulan.toLowerCase()) {
            case "januari":   return 1;
            case "februari":  return 2;
            case "maret":     return 3;
            case "april":     return 4;
            case "mei":       return 5;
            case "juni":      return 6;
            case "juli":      return 7;
            case "agustus":   return 8;
            case "september": return 9;
            case "oktober":   return 10;
            case "november":  return 11;
            case "desember":  return 12;
            default:          return 0;
        }
    }
}
