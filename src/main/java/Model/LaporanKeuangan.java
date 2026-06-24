package Model;

import java.util.ArrayList;
import java.util.List;

public class LaporanKeuangan {

    private String periode;
    private List<ModelTransaksi> daftarTransaksi;
    private List<ModelCashflow> daftarCashflow;

    public LaporanKeuangan(String periode) {
        this.periode          = periode;
        this.daftarTransaksi  = new ArrayList<>();
        this.daftarCashflow   = new ArrayList<>();
    }

    public LaporanKeuangan(String periode,
                           List<ModelTransaksi> daftarTransaksi,
                           List<ModelCashflow>  daftarCashflow) {
        this.periode         = periode;
        this.daftarTransaksi = daftarTransaksi;
        this.daftarCashflow  = daftarCashflow; 
    }

    public void tambahTransaksi(ModelTransaksi transaksi) {
        daftarTransaksi.add(transaksi);
    }

    public void hapusTransaksi(ModelTransaksi transaksi) {
        daftarTransaksi.remove(transaksi);
    }

    public List<ModelTransaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    public void tambahCashflow(ModelCashflow cashflow) {
        daftarCashflow.add(cashflow);
    }

    public List<ModelCashflow> getDaftarCashflow() {
        return daftarCashflow;
    }

    public double hitungTotalPemasukan() {
        double total = 0;
        for (ModelTransaksi t : daftarTransaksi) {
            if ("Pemasukan".equalsIgnoreCase(t.getJenis())) {
                total += t.getNominal();
            }
        }
        return total;
    }

    public double hitungTotalPengeluaran() {
        double total = 0;
        for (ModelTransaksi t : daftarTransaksi) {
            if ("Pengeluaran".equalsIgnoreCase(t.getJenis())) {
                total += t.getNominal();
            }
        }
        return total;
    }

    public double hitungSaldo() {
        return hitungTotalPemasukan() - hitungTotalPengeluaran();
    }

    public void cetakRingkasan() {
        System.out.println("===== Laporan Keuangan Periode: " + periode + " =====");
        System.out.println("-- Daftar Transaksi --");
        for (ModelTransaksi t : daftarTransaksi) {
            System.out.println(t.getRingkasan());
        }
        System.out.println("-- Ringkasan Cashflow --");
        for (ModelCashflow c : daftarCashflow) {
            System.out.println(c.getRingkasan());
        }
        System.out.printf("Total Pemasukan : Rp %.2f%n", hitungTotalPemasukan());
        System.out.printf("Total Pengeluaran: Rp %.2f%n", hitungTotalPengeluaran());
        System.out.printf("Saldo Bersih    : Rp %.2f%n", hitungSaldo());
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
