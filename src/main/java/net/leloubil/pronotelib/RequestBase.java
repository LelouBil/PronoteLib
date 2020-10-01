package net.leloubil.pronotelib;

public class RequestBase {

    public String nom;

    public String numeroOrdre;

    public long session;

    public RequestBase(String numeroOrdre, long session) {
        this.numeroOrdre = numeroOrdre;
        this.session = session;
    }

    public RequestBase(String nom, String numeroOrdre, long session) {
        this.nom = nom;
        this.numeroOrdre = numeroOrdre;
        this.session = session;
    }

    public RequestBase() {

    }
}
