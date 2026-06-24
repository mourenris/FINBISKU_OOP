package Model;

public class ModelTransaksi extends ModelKeuangan {

    public int idTransaksi;
    public String tanggal;
    public String jenis;
    public String kategori;
    public double nominal;

    public ModelTransaksi() {
        super();
    }

    public ModelTransaksi(int idTransaksi, int idUser, String tanggal,
                          String jenis, String kategori,
                          double nominal, String keterangan) {
        super(idUser, keterangan);
        this.idTransaksi = idTransaksi;
        this.tanggal     = tanggal;
        this.jenis       = jenis;
        this.kategori    = kategori;
        this.nominal     = nominal;
    }

    @Override
    public String getRingkasan() {
        return String.format("[Transaksi #%d] %s | %s | Rp %.2f | %s",
                idTransaksi, jenis, kategori, nominal, keterangan);
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getTanggal() { 
        return tanggal; 
    }

    public void setTanggal(String tanggal) { 
        this.tanggal = tanggal; 
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }
}