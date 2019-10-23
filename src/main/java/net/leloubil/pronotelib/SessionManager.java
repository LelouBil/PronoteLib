package net.leloubil.pronotelib;

import java.security.*;

class SessionManager {

    private PronoteConnection obj;

    SessionManager(int session, PronoteConnection o){
        this.obj = o;
        this.session = session;
    }

    private int session;

    private Integer orderNumber = -1;

    String getNumber() {
        orderNumber += 2;
        try {
            return obj.authManager.encryptaes(orderNumber.toString().getBytes());
        } catch (GeneralSecurityException e) {
            orderNumber -= 2;
            return "";
        }
    }

    int getSession() {
        return session;
    }
}
