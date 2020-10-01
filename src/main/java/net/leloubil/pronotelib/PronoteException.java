package net.leloubil.pronotelib;

public class PronoteException extends Exception {
    private static final long serialVersionUID = -500447172935249662L;

    public PronoteException(int errorNumber, String errorText) {
        super(errorNumber + " : " + errorText);
    }
}
