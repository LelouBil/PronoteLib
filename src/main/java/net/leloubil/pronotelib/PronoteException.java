package net.leloubil.pronotelib;

class PronoteException extends Exception {
    PronoteException(int errorNumber, String errorText) {
        super(errorNumber + " : " + errorText);
    }
}
