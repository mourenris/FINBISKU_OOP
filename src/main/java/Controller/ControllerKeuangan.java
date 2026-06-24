package Controller;

import Model.ModelTransaksi;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Koneksi.KoneksiDB;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Felda Muffarihati
 */
public class ControllerKeuangan {

    public boolean simpanTransaksi(String tgl, String jenis, String kat, double nom, String ket) {
        Connection conn = null;
        try {
            conn = KoneksiDB.getKoneksi();
            if (conn == null) { System.out.println("Koneksi null!"); return false; }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transaksi (tanggal, jenis, kategori, nominal, keterangan, id_user) VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setDate(1, java.sql.Date.valueOf(tgl));
            ps.setString(2, jenis);
            ps.setString(3, kat);
            ps.setDouble(4, nom);
            ps.setString(5, ket);
            ps.setInt(6, 1);
            ps.executeUpdate();
            ps.close();
            System.out.println("[Transaksi] Simpan berhasil.");
            return true;
        } catch (Exception e) {
            System.out.println("[Transaksi] Gagal simpan: " + e.getMessage());
            return false;
        } finally {
            tutup(conn);
            updateCashflow(tgl, 1); // selalu update cashflow setelah simpan
        }
    }

    public boolean ubahTransaksi(int id, String jenis, String kat, double nom, String ket) {
        Connection conn = null;
        String tgl = null;
        try {
            conn = KoneksiDB.getKoneksi();
            if (conn == null) { System.out.println("Koneksi null!"); return false; }

            PreparedStatement psGet = conn.prepareStatement("SELECT tanggal FROM transaksi WHERE id_transaksi=?");
            psGet.setInt(1, id);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) tgl = rs.getString("tanggal");
            psGet.close();

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE transaksi SET jenis=?, kategori=?, nominal=?, keterangan=? WHERE id_transaksi=?"
            );
            ps.setString(1, jenis);
            ps.setString(2, kat);
            ps.setDouble(3, nom);
            ps.setString(4, ket);
            ps.setInt(5, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("[Transaksi] Ubah berhasil.");
            return true;
        } catch (Exception e) {
            System.out.println("[Transaksi] Gagal ubah: " + e.getMessage());
            return false;
        } finally {
            tutup(conn);
            if (tgl != null) updateCashflow(tgl, 1);
        }
    }

    public boolean hapusTransaksi(int id) {
        Connection conn = null;
        String tgl = null;
        try {
            conn = KoneksiDB.getKoneksi();
            if (conn == null) { System.out.println("Koneksi null!"); return false; }

            PreparedStatement psGet = conn.prepareStatement("SELECT tanggal FROM transaksi WHERE id_transaksi=?");
            psGet.setInt(1, id);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) tgl = rs.getString("tanggal");
            psGet.close();

            PreparedStatement ps = conn.prepareStatement("DELETE FROM transaksi WHERE id_transaksi=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("[Transaksi] Hapus berhasil.");
            return true;
        } catch (Exception e) {
            System.out.println("[Transaksi] Gagal hapus: " + e.getMessage());
            return false;
        } finally {
            tutup(conn);
            if (tgl != null) updateCashflow(tgl, 1);
        }
    }

    /**
     * Hitung ulang total per periode lalu INSERT/UPDATE tabel cashflow.
     * Koneksi BARU dipakai di sini supaya tidak bentrok dengan koneksi sebelumnya.
     */
    private void updateCashflow(String tgl, int idUser) {
        Connection conn = null;
        try {
            String periode = tgl.substring(0, 7);
            System.out.println("[Cashflow] Memproses periode: " + periode);

            conn = KoneksiDB.getKoneksi();
            if (conn == null) { System.out.println("[Cashflow] Koneksi null!"); return; }

            // Hitung total dari transaksi
            PreparedStatement psH = conn.prepareStatement(
                "SELECT " +
                "SUM(CASE WHEN jenis='Pemasukan'   THEN nominal ELSE 0 END) AS total_pemasukan, " +
                "SUM(CASE WHEN jenis='Pengeluaran' THEN nominal ELSE 0 END) AS total_pengeluaran " +
                "FROM transaksi WHERE DATE_FORMAT(tanggal,'%Y-%m')=? AND id_user=?"
            );
            psH.setString(1, periode);
            psH.setInt(2, idUser);
            ResultSet rs = psH.executeQuery();
            double masuk = 0, keluar = 0;
            if (rs.next()) { masuk = rs.getDouble("total_pemasukan"); keluar = rs.getDouble("total_pengeluaran"); }
            psH.close();
            double saldo = masuk - keluar;
            System.out.println("[Cashflow] Pemasukan=" + masuk + " Pengeluaran=" + keluar + " Saldo=" + saldo);

            // Cek sudah ada di cashflow?
            PreparedStatement psCek = conn.prepareStatement(
                "SELECT id_cashflow FROM cashflow WHERE periode=? AND id_user=?"
            );
            psCek.setString(1, periode);
            psCek.setInt(2, idUser);
            boolean ada = psCek.executeQuery().next();
            psCek.close();

            if (ada) {
                PreparedStatement psU = conn.prepareStatement(
                    "UPDATE cashflow SET total_pemasukan=?, total_pengeluaran=?, saldo=? WHERE periode=? AND id_user=?"
                );
                psU.setDouble(1, masuk); psU.setDouble(2, keluar); psU.setDouble(3, saldo);
                psU.setString(4, periode); psU.setInt(5, idUser);
                psU.executeUpdate(); psU.close();
                System.out.println("[Cashflow] UPDATE berhasil.");
            } else {
                PreparedStatement psI = conn.prepareStatement(
                    "INSERT INTO cashflow (id_user, total_pemasukan, total_pengeluaran, saldo, periode) VALUES (?,?,?,?,?)"
                );
                psI.setInt(1, idUser); psI.setDouble(2, masuk); psI.setDouble(3, keluar);
                psI.setDouble(4, saldo); psI.setString(5, periode);
                psI.executeUpdate(); psI.close();
                System.out.println("[Cashflow] INSERT berhasil.");
            }

        } catch (Exception e) {
            System.out.println("[Cashflow] ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(conn);
        }
    }

    public ArrayList<ModelTransaksi> ambilSemuaModelTransaksi() {
        ArrayList<ModelTransaksi> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = KoneksiDB.getKoneksi();
            if (conn == null) return list;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM transaksi ORDER BY tanggal DESC");
            while (rs.next()) {
                ModelTransaksi t = new ModelTransaksi();
                t.setIdTransaksi(rs.getInt("id_transaksi"));
                t.setTanggal(rs.getString("tanggal"));
                t.setJenis(rs.getString("jenis"));
                t.setKategori(rs.getString("kategori"));
                t.setNominal(rs.getDouble("nominal"));
                t.setKeterangan(rs.getString("keterangan"));
                list.add(t);
            }
        } catch (Exception e) {
            System.out.println("Gagal ambil data: " + e.getMessage());
        } finally {
            tutup(conn);
        }
        return list;
    }

    private void tutup(Connection conn) {
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
