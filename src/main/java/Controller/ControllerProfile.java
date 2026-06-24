/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Koneksi.KoneksiDB;
import Model.ModelUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Felda Muffarihati
 */
public class ControllerProfile {
    public ModelUser getProfile(int idUserLogin) {
        ModelUser user = null;
        try {
            Connection conn = KoneksiDB.getKoneksi();
            String sql = "SELECT * FROM users WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUserLogin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new ModelUser();
                user.setIdUser(rs.getInt("id_user"));
                user.setNama(rs.getString("nama"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setNamaUsaha(rs.getString("nama_usaha"));
                user.setAlamat(rs.getString("alamat"));
                user.setDeskripsi(rs.getString("deskripsi"));
            }
        } catch (SQLException e) {
            System.out.println("Gagal ambil profil: " + e.getMessage());
        }
        return user;
    }

    public boolean simpanProfile(
            int idUserLogin,
            String nama,
            String email,
            String password,
            String usaha,
            String alamat,
            String deskripsi) {
        try {
            Connection conn = KoneksiDB.getKoneksi();
            String sql = "UPDATE users SET nama=?, email=?, password=?, nama_usaha=?, alamat=?, deskripsi=? WHERE id_user=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, usaha);
            ps.setString(5, alamat);
            ps.setString(6, deskripsi);
            ps.setInt(7, idUserLogin);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal simpan profil: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusProfile(int idUserLogin) {
        try {
            Connection conn = KoneksiDB.getKoneksi();
            String sql = "DELETE FROM users WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUserLogin);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal hapus profil: " + e.getMessage());
            return false;
        }
    }
}