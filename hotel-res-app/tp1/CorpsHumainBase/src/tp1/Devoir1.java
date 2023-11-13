package tp1;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import netscape.javascript.JSException;

public class Devoir1 {

    private static final String CMD_IMPORTER = "importer";
    private static final String CMD_EXPORTER = "exporter";
    private static final String CMD_QUITTER = "quitter";
    private static final String TYPE_XML = "xml";
    private static final String TYPE_JSON = "json";
    private static MainBody mainBody;

    public static void main(String[] args) {
        try
        {
            // Il est possible que vous ayez à déplacer la connexion ailleurs.
            // N'hésitez pas à le faire!
            BufferedReader reader = ouvrirFichier(args);
            String transaction = lireTransaction(reader);
            
            // Initialisation mainBody a null avant loop transactions
            mainBody = null;
            while (!finTransaction(transaction))
            {
                executerTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private static String getExtensionFichier(String nomFichier) {
        if(nomFichier.lastIndexOf(".") != -1 && nomFichier.lastIndexOf(".") != 0)
            return nomFichier.substring(nomFichier.lastIndexOf(".")+1);
        else return "";
    }

    /**
     * Decodage et traitement d'une transaction
     */
    static void executerTransaction(String transaction) throws Exception, IFT287Exception
    {
        try
        {
            System.out.print(transaction + " ");
            // Decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens())
            {
                String mode = tokenizer.nextToken();
                String nomFichier = readString(tokenizer);
                String extension = getExtensionFichier(nomFichier);
                
                if (mode.equals(CMD_IMPORTER)){
                    // reset mainBody a nulle avant importation
                    mainBody = null;
                    if(extension.equals(TYPE_XML)){
                        // Lecture fichier xml avec SAXP et handler custom
                        System.out.println("Debut de l'importation du fichier XML " + nomFichier);
                        mainBody = ImporterXml(nomFichier);
                    }
                    else if (extension.equals(TYPE_JSON)){
                        System.out.println("Debut de l'importation du fichier JSON " + nomFichier);
                        //Votre code d'importation JSON ici (Partie 4)
                        mainBody = ImporterJson(nomFichier);
                    }
                    else {
                        System.out.println("Le système ne supporte actuellement pas l'importation des fichiers au format " + extension);
                    }
                }
                else if (mode.equals(CMD_EXPORTER)){
                    if(extension.equals(TYPE_XML)){
                        System.out.println("Debut de l'exportation vers le fichier XML " + nomFichier);
                        // Votre code d'exportation XML ici (Partie 4)
                        ExporterXml(nomFichier);

                    }
                    else if (extension.equals(TYPE_JSON)){
                        System.out.println("Debut de l'exportation vers le fichier JSON " + nomFichier);
                        ExporterJson(nomFichier);
                    }
                    else {
                        System.out.println("Le système ne supporte actuellement pas l'exportation vers les fichiers au format " + extension);
                    }
                }
                else{
                    System.out.println("Commande inconnue, choisir entre 'importer' ou 'exporter'");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(" " + e.toString());
        }
    }

    private static MainBody ImporterXml(String nomFichier) throws IllegalStateException, Exception {
        MainBody mainBody = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XmlHandlerParser xmlHandlerParser = new XmlHandlerParser();
            
            // Parse le xml et extraire instance mainBody
            saxParser.parse(nomFichier, xmlHandlerParser);
            mainBody = xmlHandlerParser.getMainBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Check pour parse OK mais instance est vide
        if (mainBody == null) {
            throw new IllegalStateException("Parse complet, mais instance mainBody est null.");
        }
        return mainBody;
    }

    private static MainBody ImporterJson(String nomFichier) throws JSException {
        MainBody mainBody = null;
        JsonParserMainBody jsonParserMainBody = new JsonParserMainBody();
        try {
            mainBody = jsonParserMainBody.parseJsonMainBody(nomFichier);
        } catch (JsonException je) {
            je.printStackTrace();
        }
        return mainBody;
    }

    private static void ExporterJson(
        String nomFichier
    ) throws JsonException, IllegalStateException, Exception {
        // Construction objet Json a partir de instance mainBody
        JsonObject mainBodyJson = null;
        MainBodyJsonBuilder mainBodyJsonBuilder = new MainBodyJsonBuilder();
        try {
            mainBodyJsonBuilder.buildMainBodyJson(mainBody);
        } catch (JsonException je) {
            je.printStackTrace();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainBodyJson = mainBodyJsonBuilder.getMainBodyJson();
        if (mainBodyJson == null) {
            throw new IllegalStateException("mainBodyJson est null");
        }
        
        // Configuration pour pretty print
        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        // Ecrire Json dans fichier
        try (FileWriter fileWriter = new FileWriter(nomFichier);
            JsonWriter jsonWriter = writerFactory.createWriter(fileWriter)) {
            jsonWriter.writeObject(mainBodyJson);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void ExporterXml(
        String nomFichier
        ) throws Exception, IOException {
        // Construction document vide
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        Document document = f.newDocumentBuilder().newDocument();

        // Populer le document avec instance mainBody
        MainBodyXmlBuilder mainBodyXmlBuilder = new MainBodyXmlBuilder(document);
        mainBodyXmlBuilder.buildMainBodyDocument(mainBody);
        document = mainBodyXmlBuilder.getDocument();

        // Transformer l'object document popule vers output XML
        try {
            FileOutputStream output = new FileOutputStream(nomFichier);
            PrintStream out = new PrintStream(output);

            TransformerFactory allSpark = TransformerFactory.newInstance();
            Transformer optimusPrime = allSpark.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(out);
            optimusPrime.transform(source, result);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ****************************************************************
    // *   Les methodes suivantes n'ont pas besoin d'etre modifiees   *
    // ****************************************************************

    /**
     * Ouvre le fichier de transaction, ou lit à partir de System.in
     */
    public static BufferedReader ouvrirFichier(String[] args) throws FileNotFoundException {
        if (args.length < 1)
            // Lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // Lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException
    {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        return (transaction == null || transaction.equals(CMD_QUITTER));
    }

    /** Lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("Autre parametre attendu");
    }

}
