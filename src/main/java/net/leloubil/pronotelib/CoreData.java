package net.leloubil.pronotelib;

public class CoreData {

    public String nom;

    public String numeroOrdre;

    public long session;


    public CoreData( String numeroOrdre, long session) {
        this.numeroOrdre = numeroOrdre;
        this.session = session;
    }

    public CoreData(String nom, String numeroOrdre, long session) {
        this.nom = nom;
        this.numeroOrdre = numeroOrdre;
        this.session = session;
    }
    public CoreData(){

    }
}
