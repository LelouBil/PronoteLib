package net.leloubil.pronotelib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.leloubil.pronotelib.entities.DateDeserializer;
import net.leloubil.pronotelib.entities.EDT;
import net.leloubil.pronotelib.entities.Lesson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class PronoteConnection {

    AuthManager authManager = new AuthManager(this);

    private SessionManager sessionManager;

    public PronoteConnection(String url){
	    this.setUrl(url);
    }

    public EDT getEmploiDuTemps(int semaine) {
        JsonNode jsonedt;
        try {
            jsonedt = navigate(PagesType.PageEmploiDuTemps, Collections.singletonMap("NumeroSemaine", semaine)).get("donneesSec").get("donnees");
        } catch (PronoteException e) {
            e.printStackTrace();
            return null;
        }
        SimpleModule module =
                new SimpleModule("LongDeserializerModule",
                        new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Lesson.class, new Lesson.LessonDeserializer(this));
        module.addDeserializer(Date.class,new DateDeserializer());
        ObjectMapper om = new ObjectMapper();
        om.registerModule(module);
        try {
            return om.treeToValue(jsonedt, EDT.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private JsonNode appelFonction(String foncName) {
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

    public boolean login(String user,String pass){
        return initEncryption() && requestAuth(user,pass);
    }

    private boolean requestAuth(String user, String pass) {
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
        String alea = result.get("alea").asText();
        String challenge = result.get("challenge").asText();
        if(authManager.doChallenge(user, pass, alea, challenge)){
            getAcceuil();
	    return true;
	    }
	    else return false;
    }

    private void getAcceuil() {
        navigate(PagesType.PageAccueil);
        //System.out.println("Connexion terminée");
    }



    private JsonNode navigate(PagesType page, Map<String, Object> content) throws PronoteException {
	HashMap m = new HashMap();
        m.put("onglet",page.getId());
        m.put("ongletPrec", last);
        appelFonction("Navigation", m);
        last = page;
        JsonNode n = appelFonction(page.name(), content);
        if(n.get("Erreur") != null) {
            n = n.get("Erreur");
            int errorNumber = n.get("G").asInt();
            String errorText = n.get("Titre").asText();
            throw new PronoteException(errorNumber,errorText);
        }
        return n;
    }


    private JsonNode navigate(@NotNull PagesType page) {
        HashMap m = new HashMap();
        m.put("onglet", page.getId());
        m.put("ongletPrec", last);
        appelFonction("Navigation", m);
        return appelFonction(page.name());
    }


    public JsonNode appelFonction(String foncName, Map<String, Object> data) {
        try {
            final JsonNodeFactory factory = JsonNodeFactory.instance;
            String number = sessionManager.getNumber();
            ObjectMapper om = new ObjectMapper();
            JsonNode json = om.valueToTree(new RequestBase(foncName, number, sessionManager.getSession()));
            ((ObjectNode) json).set("donneesSec", factory.objectNode().set("donnees", om.valueToTree(data)));
                ((ObjectNode) json.get("donneesSec")).set("_Signature_", factory.objectNode().set("onglet", factory.numberNode(last.getId())));
            String finjs = om.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            Request r = new Request.Builder().url(apiUrl + sessionManager.getSession() + "/" + number)
                    .post(RequestBody.create(finjs, JSON)).build();

            Response res = client.newCall(r).execute();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            ResponseBody rb = res.body();
            if (rb == null) return null;
            String body = rb.string();
            return om.readTree(body);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    private boolean initEncryption() {
        Request r = new Request.Builder().url(portalUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").build();
        try {
            Response response = client.newCall(r).execute();
            ResponseBody rb = response.body();
            if(rb == null) return false;
            Matcher matcher = regex.matcher(rb.string());
            if (!matcher.find()) {
                return false;
            }
            String params = matcher.group(2);
            ObjectMapper om = new ObjectMapper();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            JsonNode n = om.readTree(params);
            sessionManager = new SessionManager(n.get("h").asInt(),this);
            String mr = n.get("MR").asText();
            String er = n.get("ER").asText();
            return authManager.sendIv(mr,er);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String apiUrl = "";

    private static Pattern regex = Pattern.compile("(<body id=\"id_body\" role=\"application\" onload=\"try . Start )\\((.+)\\) . catch");

    private OkHttpClient client = new OkHttpClient();

    private String portalUrl;



    private PagesType last = PagesType.PageAccueil;

    private void setUrl(String url) {
        portalUrl = url;
        apiUrl = url.replace("eleve.html", "appelfonction/3/");
    }
}
