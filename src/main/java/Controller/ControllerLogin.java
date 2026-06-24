/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ModelUser;

/**
 *
 * @author Felda Muffarihati
 */
public class ControllerLogin {

    private static ModelUser userLogin;

    public boolean login(String email, String password) {

        if (email.equals("admin@gmail.com")
                && password.equals("admin123")) {

            userLogin = new ModelUser();

            userLogin.setIdUser(1);
            userLogin.setNama("Admin");
            userLogin.setEmail(email);
            userLogin.setPassword(password);
            userLogin.setNamaUsaha("FINBISKU");

            return true;
        }

        return false;
    }

    public ModelUser getUserLogin() {
        return userLogin;
    }

    public void logout() {
        userLogin = null;
    }
}