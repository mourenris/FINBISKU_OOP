/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Felda Muffarihati
 */
    public class ModelRekapKategori extends ModelKeuangan{
    
        private String kategori;
        private String jenis;
        private double total;

        public ModelRekapKategori() {
            super();
        }

        public ModelRekapKategori(String kategori, String jenis, double total) {
            super(0, "Rekap " + jenis + " - " + kategori);
            this.kategori = kategori;
            this.jenis    = jenis;
            this.total    = total;
        }

        @Override
        public String getRingkasan() {
            return String.format("[Rekap] %s | %s | Total: Rp %.2f", kategori, jenis, total);
        }

        public String getKategori() { return kategori; }
        public void setKategori(String k) { this.kategori = k; }

        public String getJenis() { return jenis; }
        public void setJenis(String j) { this.jenis = j; }

        public double getTotal() { return total; }
        public void setTotal(double t) { this.total = t; }
    }

