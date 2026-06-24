package Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Felda Muffarihati
 */
public class KoneksiDB {

    private static final String URL  = "jdbc:mysql://localhost:3306/finbisku_oop";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getKoneksi() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi Berhasil!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }
}
