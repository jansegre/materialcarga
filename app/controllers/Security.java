package controllers;

import models.*;

public class Security extends Secure.Security {

    static boolean authentify(String username, String password) {
        return Usuario.connect(username, password) != null;
    }
    
    static boolean check(String profile) {
        if("admin".equals(profile)) {
            return Usuario.find("byNome", connected()).<Usuario>first().isAdmin;
        }
        return false;
    }
    
    static void onDisconnected() {
        Application.index();
    }
    
    static void onAuthenticated() {
        Admin.index();
    }
    
}
