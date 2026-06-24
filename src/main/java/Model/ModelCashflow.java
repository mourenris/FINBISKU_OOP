package Model;

public class ModelCashflow extends ModelKeuangan {

    private int    idCashflow;
    private double totalPemasukan;
    private double totalPengeluaran;
    private double saldo;
    private String periode;

    public ModelCashflow() {
        super();
    }

    public ModelCashflow(int idCashflow, int idUser, double totalPemasukan,
                         double totalPengeluaran, double saldo,
                         String periode, String keterangan) {
        super(idUser, keterangan);
        this.idCashflow       = idCashflow;
        this.totalPemasukan   = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo            = saldo;
        this.periode          = periode;
    }

    @Override
    public String getRingkasan() {
        return String.format("[Cashflow %s] Pemasukan: Rp %.2f | Pengeluaran: Rp %.2f | Saldo: Rp %.2f",
                periode, totalPemasukan, totalPengeluaran, saldo);
    }

    public int getIdCashflow() {
        return idCashflow;
    }

    public void setIdCashflow(int idCashflow) {
        this.idCashflow = idCashflow;
    }

    public double getTotalPemasukan() {
        return totalPemasukan;
    }

    public void setTotalPemasukan(double totalPemasukan) {
        this.totalPemasukan = totalPemasukan;
    }

    public double getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public void setTotalPengeluaran(double totalPengeluaran) {
        this.totalPengeluaran = totalPengeluaran;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
