package net.leloubil.betterpronote;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class Main {



    public static void main(String[] args) {

        ObjetCommunication.setUrl(args[0]);
        ObjetCommunication.identificate(args[1].toLowerCase(),args[2]);

        HashMap<String,Object> map = new HashMap<>();
        HashMap<String,Object> domaine = new HashMap<>();
        domaine.put("V","[1..62]");
        domaine.put("_T",8);
        map.put("domaine", domaine);
        ObjetCommunication.navigate("PageCahierDeTexte",map);
    }


}
