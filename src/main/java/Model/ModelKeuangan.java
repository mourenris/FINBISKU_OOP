package Model;


public abstract class ModelKeuangan {

    public int idUser;
    public String keterangan;

    public ModelKeuangan() {
    }

    public ModelKeuangan(int idUser, String keterangan) {
        this.idUser      = idUser;
        this.keterangan  = keterangan;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public abstract String getRingkasan();
}
