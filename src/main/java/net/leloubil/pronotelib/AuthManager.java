package net.leloubil.pronotelib;

import com.fasterxml.jackson.databind.JsonNode;
import net.leloubil.pronotelib.entities.Periode;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

class AuthManager {

    AuthManager(PronoteConnection obj) {
        this.obj = obj;
    }

    private PronoteConnection obj;

    private  byte[] debyte(byte[] b) {
        String s = new String(b);
        String[] arr = s.split(",");
        byte[] out = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = (byte) Integer.parseInt(arr[i]);
        }
        return out;
    }

    private String removeRnd(String st) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < st.length(); i++) {
            if (i % 2 == 0) s.append(st.charAt(i));
        }
        return s.toString();
    }

    private String hashPass(String mdp, String withRnd) {
        MessageDigest passDigest;
        try {
            passDigest = MessageDigest.getInstance(SHA_256);
            passDigest.update(withRnd.getBytes());
            passDigest.update(mdp.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(passDigest.digest(), false);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }



    private byte[] encrypt(byte[] data, PublicKey publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private byte[] decryptAes(String plaintext, byte[] key) throws GeneralSecurityException{
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(MessageDigest.getInstance(MD5).digest(iv));
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(Hex.decodeHex(plaintext));
        } catch (DecoderException e){
            throw new GeneralSecurityException("hex encode error");
        }
    }

    private byte[] key = "".getBytes();

    boolean doChallenge(String user, String mdp, String rnd, String challenge) {
        HashMap<String,Object> m = new HashMap<>();
        m.put("connexion", 0);
        m.put("espace", 3);
        byte[] userKey = (user + hashPass(mdp, rnd)).getBytes(StandardCharsets.UTF_8);
        byte[] out;
        try {
            //System.out.println(challenge);
            out = decryptAes(challenge,userKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
        String outt = removeRnd(new String(out,StandardCharsets.UTF_8));
        JsonNode authResult;
        

        try{
            //maybe wrong key
            String finalChall = encryptaes(outt.getBytes(StandardCharsets.UTF_8),userKey);
            m.put("challenge", finalChall);
            
            //authResult only contains key, libelle utile and last connection time
            authResult = obj.appelFonction("Authentification", m);
            authResult = authResult.get("donneesSec").get("donnees");
            
            
                       		

        }
        catch(GeneralSecurityException e){
            e.printStackTrace();
            return false;
        }
        if (authResult.get("Acces") != null) {
            //System.out.println("Erreur d'authentitifaction");
            return false;
        }
        try {
            this.key = debyte(decryptAes(authResult.get("cle").asText(),userKey));
            return true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }


    }


    private byte[] iv = new byte[16];

    private byte[] tempIv = new byte[16];

    private String getUUID(String mr, String er) throws GeneralSecurityException {
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(mr, 16), new BigInteger(er, 16));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pub = factory.generatePublic(keySpec);
        new Random().nextBytes(tempIv);
        return Base64.getEncoder().encodeToString(encrypt(tempIv,pub));
    }


    String encryptaes(byte[] plaintext) throws GeneralSecurityException{
        return encryptaes(plaintext,key);
    }

    private String encryptaes(byte[] plaintext, byte[] key) throws GeneralSecurityException{
        IvParameterSpec ivSpec =new IvParameterSpec( tempIv == iv ? MessageDigest.getInstance(MD5).digest(iv) : iv);
        SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return  Hex.encodeHexString(cipher.doFinal(plaintext));
    }

    boolean sendIv(String mr, String er){
        try {
            HashMap<String,Object> s = new HashMap<>();
            s.put("Uuid",getUUID(mr,er));
            obj.appelFonction("FonctionParametres", s);
            iv = tempIv;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
