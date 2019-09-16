package net.leloubil.pronotelib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

@SuppressWarnings("unchecked")
public class ObjetCommunication {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String cookie = "";
    private static Map<String,Integer> Onglets = Stream.of(new Object[][] {
            { "PageAccueil", 7 },
            { "PageCahierDeTexte", 89 },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));;

    public static JsonNode appelFonction(String foncName){
        return appelFonction(foncName,new HashMap<>());
    }

    public static HashMap<String,Object> getDate(){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        String f = format.format(d) +" 0:0:0";
        HashMap out = new HashMap();
        out.put("_T",7);
        out.put("V",f);
        return out;
    }

    public static void identificate(String user, String pass){
        ObjetCommunication.init();
        HashMap m = new HashMap();
        m.put("genreConnexion",0);
        m.put("genreEspace",3);
        m.put("identifiant",user);
        m.put("pourENT",false);
        m.put("enConnexionAuto",false);
        m.put("demandeConnexionAUto",false);
        m.put("enConnexionAppliMobile",false);
        m.put("demandeConnexionAppliMobile",false);
        m.put("demandeConnexionAppliMobileJeton",false);
        m.put("uuidAppliMobile","");
        m.put("loginTokenSAV","");
        JsonNode result = appelFonction("Identification",m).get("donneesSec").get("donnees");

        if(result == null) return;
        if(result.get("Erreur") != null){
            System.out.println("Trop d'erreurs");
            return;
        }
        int genre = result.get("modeCompLog").asInt();
        int mdpGenre = result.get("modeCompMdp").asInt();
        String alea = result.get("alea").asText();
        String challenge = result.get("challenge").asText();
        doChallenge(user,pass,alea,challenge);
        auth = true;
        getAcceuil();
    }

    private static void getAcceuil() {
        ObjetCommunication.navigate("PageAccueil");
        System.out.println("Connexion terminée");
    }

    public static boolean auth = false;

    public static JsonNode navigate(String page,Map<String,Object> content) {
        HashMap m = new HashMap();
        int o = Onglets.get(page);
        m.put("onglet",o);
        m.put("ongletPrec",last);
        appelFonction("Navigation",m);
        last = o;
        return appelFonction(page,content);
    }

    public static JsonNode navigate(String page) {
        HashMap m = new HashMap();
        m.put("onglet",Onglets.get(page));
        m.put("ongletPrec",last);
        appelFonction("Navigation",m);
        return appelFonction(page);
    }

    public static HashMap<String,String> cookies = new HashMap<>();

    public static String cookieName = "CASTGC";


    private static void doChallenge(String user, String mdp,String alea,String challenge) {
        HashMap m = new HashMap();
        m.put("connexion",0);
        m.put("espace",3);
        String key = user + hashPass(mdp,alea);
        byte[] out = new byte[0];
        try {
            System.out.println(challenge);
            out = decryptaes(Hex.decodeHex(challenge),key.getBytes(StandardCharsets.UTF_8),iv);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        String outt = removealea(decode(out));
        String finalchal = Hex.encodeHexString(encryptaes(encode(outt),key.getBytes(),iv));
        m.put("challenge",finalchal);
        JsonNode result = appelFonction("Authentification",m).get("donneesSec").get("donnees");
        if(result.get("Acces") != null){
            System.out.println("Erreur d'authentitifaction");
            return;
        }
        try {
            numeroOrdre = 5;
            ObjetCommunication.key = debyte(decryptaes(Hex.decodeHex(result.get("cle").asText()),key.getBytes(StandardCharsets.UTF_8),iv));
            incnum();
        } catch (DecoderException e) {
            e.printStackTrace();
        }


    }

    public static byte[]debyte(byte[] b){
        String s = new String(b);
        String[] arr = s.split(",");
        byte[] out = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = (byte) Integer.parseInt(arr[i]);
        }
        return out;
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String decode(byte[] bytes){
        return new String(bytes,StandardCharsets.UTF_8);
    }

    public static byte[] encode(String bytes){
        return bytes.getBytes(StandardCharsets.UTF_8);
    }

    private static String removealea(String st) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < st.length(); i++) {
            if(i% 2 == 0) s.append(st.charAt(i));
        }
        return s.toString();
    }

    public static int getBit(byte b, int position)
    {
        return (b >> position) & 1;
    }

    public static String hashPass(String mdp, String alea) {
        MessageDigest passdigest = null;
        try {
            passdigest = MessageDigest.getInstance(SHA_256);
            passdigest.update(alea.getBytes());
            passdigest.update(mdp.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(passdigest.digest(),false);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonNode appelFonction(String foncName, Map<String,Object> data){
        try {
            final JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectMapper om = new ObjectMapper();
            JsonNode json = om.valueToTree(new CoreData(foncName,strnum,session));
            ((ObjectNode)json).set("donneesSec",factory.objectNode().set("donnees",om.valueToTree(data)));
            if(auth){
                ((ObjectNode) json.get("donneesSec")).set("_Signature_",factory.objectNode().set("onglet",factory.numberNode(last)));
            }
            String finjs = om.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println("Request : " + finjs);
            Request r = new Request.Builder().url(API + session + "/" + strnum).header("Cookie",cookieName + "=" + cookie)
                    .post(RequestBody.create(finjs,JSON)).build();

            Response res = client.newCall(r).execute();
            if(!res.headers("Set-Cookie").isEmpty()){
                cookie = res.headers("Set-Cookie").get(0).split("=")[1].split(";")[0];
            }
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            String body = res.body().string();
            JsonNode out = om.readTree(body);
            System.out.println("Response : " + om.writerWithDefaultPrettyPrinter().writeValueAsString(out));
            incnum();
            return out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void incnum() {
        numeroOrdre+= 2;
        try {
            strnum = getNumber();
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public static String strnum;

    public static void init(){
        ObjetCommunication.getMainData();
        try {
            ObjetCommunication.computeEncryption();
           strnum = ObjetCommunication.getNumber();
            HashMap<String,Object> s = new HashMap<>();
            s.put("Uuid",ObjetCommunication.UUID);
            ObjetCommunication.appelFonction("FonctionParametres",s);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static void getMainData() {
        Request r = new Request.Builder().url(URL)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").build();
        try {
            Response response = client.newCall(r).execute();
            Matcher matcher = regex.matcher(response.body().string());
            if(!matcher.find()){
                return;
            }
            String params = matcher.group(2);
            ObjectMapper om = new ObjectMapper();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            Map<String, Object> map = om.readValue(params, Map.class);
            session =  Integer.parseInt((String) map.get("h"));
            genreEspace = (Integer) map.get("a");
            MR = (String) map.get("MR");
            ER = (String) map.get("ER");
//            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

    public static void computeEncryption() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        //modulo => MR, 16
        //exposant => ER, 16

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(MR,16),new BigInteger(ER,16));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pub = factory.generatePublic(keySpec);
        rndiv = new byte[16];
        new Random().nextBytes(rndiv);
        UUID = Base64.getEncoder().encodeToString(encrypt(rndiv,pub));
        byte[] data = pub.getEncoded();
        String base64encoded = Base64.getEncoder().encodeToString(data);
        iv = rndiv;
    }

    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] rndiv;

    public static String striv = null;

    public static byte[] decryptaes(byte[] plaintext,byte[] key, byte[] ivv){
        try {
            IvParameterSpec iv =new IvParameterSpec(MessageDigest.getInstance(MD5).digest(ivv));
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return cipher.doFinal(plaintext);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptaes(byte[] plaintext,byte[] key, byte[] ivv){
        try {
            IvParameterSpec iv =new IvParameterSpec(MessageDigest.getInstance(MD5).digest(ivv));
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            return  cipher.doFinal(plaintext);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getNumber() throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        byte[] plaintext = numeroOrdre.toString().getBytes();
        IvParameterSpec iv;
        byte[] rndiv = MessageDigest.getInstance(MD5).digest(ObjetCommunication.rndiv);
        iv = new IvParameterSpec(numeroOrdre > 1 ? rndiv : new byte[16]);
        byte[] key = ObjetCommunication.key;
        SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key),"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(plaintext);
        return Hex.encodeHexString(encrypted,true);
    }
    public static byte[] key = "".getBytes();
    public static Integer numeroOrdre = 1;


    public static String URL = "";

    public static String API = "";

    static CoreData data = new CoreData();

    static Pattern regex = Pattern.compile("(<body id=\"id_body\" role=\"application\" onload=\"try . Start )\\((.+)\\) . catch");

    static OkHttpClient client = new OkHttpClient();

    public static int session = 0;

    public static int genreEspace = 3;

    public static String MR = "";

    public static String ER = "";

    static String UUID;
    public static byte[] iv; //todo celui la

    static int last = 7;

    public static void setUrl(String url) {
        URL = url;
        API = url.replace("eleve.html","appelfonction/3/");
    }
}
