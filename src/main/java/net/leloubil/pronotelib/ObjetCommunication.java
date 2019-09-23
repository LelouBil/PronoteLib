package net.leloubil.pronotelib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.leloubil.pronotelib.entities.Lesson;
import net.leloubil.pronotelib.entities.EDT;
import okhttp3.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.EncoderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
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

    public ObjetCommunication(String url){
	this.setUrl(url);
    }

    public EDT getEmploiDuTemps(int semaine) {
        JsonNode jsonedt = navigate(PagesType.PageEmploiDuTemps, Collections.singletonMap("NumeroSemaine", semaine)).get("donneesSec").get("donnees");
        SimpleModule module =
                new SimpleModule("LongDeserializerModule",
                        new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Lesson.class, new Lesson.LessonDeserializer());
        ObjectMapper om = new ObjectMapper();
        om.registerModule(module);
        try {
            return om.treeToValue(jsonedt, EDT.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JsonNode appelFonction(String foncName) {
        return appelFonction(foncName, new HashMap<>());
    }

    public enum PagesType {
        PageAccueil(7),
        PageCahierDeTexte(89),
        PageEmploiDuTemps(16);

        PagesType(int id){
            this.id = id;
        }

        private int id;

        public int getId() {
            return id;
        }
    }

    public boolean identificate(String user, String pass) {
        if(!init()) return false;
        HashMap m = new HashMap();
        m.put("genreConnexion", 0);
        m.put("genreEspace", 3);
        m.put("identifiant", user);
        m.put("pourENT", false);
        m.put("enConnexionAuto", false);
        m.put("demandeConnexionAUto", false);
        m.put("enConnexionAppliMobile", false);
        m.put("demandeConnexionAppliMobile", false);
        m.put("demandeConnexionAppliMobileJeton", false);
        m.put("uuidAppliMobile", "");
        m.put("loginTokenSAV", "");
        JsonNode result = appelFonction("Identification", m);
        if(result == null) return false;
        if (result.get("Erreur") != null)  return false;
	    result = result.get("donneesSec").get("donnees");
        int genre = result.get("modeCompLog").asInt();
        int mdpGenre = result.get("modeCompMdp").asInt();
        String alea = result.get("alea").asText();
        String challenge = result.get("challenge").asText();
        if(doChallenge(user, pass, alea, challenge)){
            auth = true;
            getAcceuil();
	    return true;
	    }
	    else return false;
    }

    private void getAcceuil() {
        navigate(PagesType.PageAccueil);
        //System.out.println("Connexion terminée");
    }

    public boolean auth = false;

    public JsonNode navigate(PagesType page,Map<String,Object> content) {
        if(!auth) return null;
	HashMap m = new HashMap();
        m.put("onglet",page.getId());
        m.put("ongletPrec", last);
        appelFonction("Navigation", m);
        last = page;
        return appelFonction(page.name(), content);
    }

    public JsonNode navigate(PagesType page) {
        HashMap m = new HashMap();
        m.put("onglet", page.getId());
        m.put("ongletPrec", last);
        appelFonction("Navigation", m);
        return appelFonction(page.name());
    }

    private boolean doChallenge(String user, String mdp, String alea, String challenge) {
        HashMap m = new HashMap();
        m.put("connexion", 0);
        m.put("espace", 3);
        String key = user + hashPass(mdp, alea);
        byte[] out = new byte[0];
        try {
            //System.out.println(challenge);
            out = decryptaes(challenge, key.getBytes(StandardCharsets.UTF_8), iv);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
	    return false;
        }
        String outt = removealea(new String(out));
        JsonNode result = null;
	try{
            String finalchal = encryptaes(outt.getBytes(), key.getBytes(), iv);
            m.put("challenge", finalchal);
            result = appelFonction("Authentification", m).get("donneesSec").get("donnees");
        }
        catch(GeneralSecurityException e){
            e.printStackTrace();
            return false;
        }
        if (result.get("Acces") != null) {
            //System.out.println("Erreur d'authentitifaction");
            return false;
        }
        try {
            this.key = debyte(decryptaes(result.get("cle").asText(), key.getBytes(StandardCharsets.UTF_8), iv));
            return true;
	} catch (GeneralSecurityException e) {
            e.printStackTrace();
	    return false;
        }


    }

    private  byte[] debyte(byte[] b) {
        String s = new String(b);
        String[] arr = s.split(",");
        byte[] out = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = (byte) Integer.parseInt(arr[i]);
        }
        return out;
    }

    private String removealea(String st) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < st.length(); i++) {
            if (i % 2 == 0) s.append(st.charAt(i));
        }
        return s.toString();
    }

    private String hashPass(String mdp, String alea) {
        MessageDigest passdigest = null;
        try {
            passdigest = MessageDigest.getInstance(SHA_256);
            passdigest.update(alea.getBytes());
            passdigest.update(mdp.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(passdigest.digest(), false);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JsonNode appelFonction(String foncName, Map<String, Object> data) {
        try {
            incnum();
            //System.out.println(numeroOrdre);
            final JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectMapper om = new ObjectMapper();
            JsonNode json = om.valueToTree(new RequestBase(foncName, strnum, session));
            ((ObjectNode) json).set("donneesSec", factory.objectNode().set("donnees", om.valueToTree(data)));
            if (auth) {
                ((ObjectNode) json.get("donneesSec")).set("_Signature_", factory.objectNode().set("onglet", factory.numberNode(last.getId())));
            }
            String finjs = om.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            //System.out.println("Request : " + finjs);
            Request r = new Request.Builder().url(API + session + "/" + strnum)
                    .post(RequestBody.create(finjs, JSON)).build();

            Response res = client.newCall(r).execute();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            String body = res.body().string();
            JsonNode out = om.readTree(body);
            //System.out.println("Response : " + om.writerWithDefaultPrettyPrinter().writeValueAsString(out));

            return out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void incnum() {
        numeroOrdre += 2;
        try {
            strnum = getNumber();
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public String strnum;

    public boolean init() {
        if(!getMainData()) return false;
        try {
            computeEncryption();
             HashMap<String,Object> s = new HashMap<>();
            s.put("Uuid",UUID);
            appelFonction("FonctionParametres", s);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean getMainData() {
        Request r = new Request.Builder().url(URL)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").build();
        try {
            Response response = client.newCall(r).execute();
            Matcher matcher = regex.matcher(response.body().string());
            if (!matcher.find()) {
                return false;
            }
            String params = matcher.group(2);
            ObjectMapper om = new ObjectMapper();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            Map<String, Object> map = om.readValue(params, Map.class);
            session = Integer.parseInt((String) map.get("h"));
            genreEspace = (Integer) map.get("a");
            MR = (String) map.get("MR");
            ER = (String) map.get("ER");
//            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void computeEncryption() throws GeneralSecurityException {
        //modulo => MR, 16
        //exposant => ER, 16
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(MR, 16), new BigInteger(ER, 16));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pub = factory.generatePublic(keySpec);
        new Random().nextBytes(this.iv);
        UUID = Base64.getEncoder().encodeToString(encrypt(this.iv,pub));
    }

    private byte[] encrypt(byte[] data, PublicKey publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private byte[] decryptaes(String plaintext,byte[] key, byte[] ivv) throws GeneralSecurityException{
        try {
            IvParameterSpec iv = new IvParameterSpec(MessageDigest.getInstance(MD5).digest(ivv));
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return cipher.doFinal(Hex.decodeHex(plaintext));
        } catch (DecoderException e){
            throw new GeneralSecurityException("hex encode error");
        }
    }

    private String encryptaes(byte[] plaintext,byte[] key, byte[] ivv) throws GeneralSecurityException{
        IvParameterSpec iv =new IvParameterSpec(MessageDigest.getInstance(MD5).digest(ivv));
        SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        return  Hex.encodeHexString(cipher.doFinal(plaintext));
    }

    private String getNumber() throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        byte[] plaintext = numeroOrdre.toString().getBytes();
        IvParameterSpec iva;
        iva = new IvParameterSpec(numeroOrdre > 1 ? MessageDigest.getInstance(MD5).digest(iv) : new byte[16]);
        byte[] key = this.key;
        SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(MD5).digest(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iva);
        byte[] encrypted = cipher.doFinal(plaintext);
        return Hex.encodeHexString(encrypted, true);
    }

    private byte[] key = "".getBytes();
    private Integer numeroOrdre = -1;


    private String URL = "";

    private String API = "";

    static Pattern regex = Pattern.compile("(<body id=\"id_body\" role=\"application\" onload=\"try . Start )\\((.+)\\) . catch");

    private OkHttpClient client = new OkHttpClient();

    private int session = 0;

    private int genreEspace = 3;

    private String MR = "";

    private String ER = "";

    String UUID;
    private byte[] iv = new byte[16]; //todo celui la

    private PagesType last = PagesType.PageAccueil;

    public void setUrl(String url) {
        URL = url;
        API = url.replace("eleve.html", "appelfonction/3/");
    }
}
