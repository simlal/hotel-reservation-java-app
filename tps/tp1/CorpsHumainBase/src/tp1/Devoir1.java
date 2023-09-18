package tp1;

import java.io.*;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Devoir1 {

    private static final String CMD_IMPORTER = "importer";
    private static final String CMD_EXPORTER = "exporter";
    private static final String CMD_QUITTER = "quitter";
    private static final String TYPE_XML = "xml";
    private static final String TYPE_JSON = "json";

    public static void main(String[] args) {
        try
        {
            // Il est possible que vous ayez à déplacer la connexion ailleurs.
            // N'hésitez pas à le faire!
            BufferedReader reader = ouvrirFichier(args);
            String transaction = lireTransaction(reader);
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
                    if(extension.equals(TYPE_XML)){
                        System.out.println("Debut de l'importation du fichier XML " + nomFichier);
                        // Lecture fichier xml avec SAXP et handler custom
                        try {
                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            SAXParser saxParser = factory.newSAXParser();
                            XmlHandlerParser handler = new XmlHandlerParser();
                            
                            saxParser.parse(nomFichier, handler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else if (extension.equals(TYPE_JSON)){
                        System.out.println("Debut de l'importation du fichier JSON " + nomFichier);
                        //Votre code d'importation JSON ici (Partie 4)


                    }
                    else {
                        System.out.println("Le système ne supporte actuellement pas l'importation des fichiers au format " + extension);
                    }
                }
                else if (mode.equals(CMD_EXPORTER)){
                    if(extension.equals(TYPE_XML)){
                        System.out.println("Debut de l'exportation vers le fichier XML " + nomFichier);
                        // Votre code d'exportation XML ici (Partie 4)


                    }
                    else if (extension.equals(TYPE_JSON)){
                        System.out.println("Debut de l'exportation vers le fichier JSON " + nomFichier);
                        //Votre code d'exportation JSON ici (Partie 3)


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
