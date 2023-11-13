// Travail fait par :
//   NomEquipier1 - Matricule
//   NomEquipier2 - Matricule

/**
 * Fichier de base pour le Devoir1A du cours IFT287
 *
 * <pre>
 * 
 * Vincent Ducharme
 * Universite de Sherbrooke
 * Version 1.0 - 6 août 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet de convertir un fichier XML en son équivalent en JSON.
 *
 * Paramètres du programme
 * 0- Nom du fichier XML
 * 1- Nom du fichier JSON
 * 
 * </pre>
 */

package tp1;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConversionXmlJson {

    public static void main(String[] args)
    {
        if (args.length < 2 || args.length > 2)
        {
            System.out.println("Usage: java tp1.Devoir1A <fichierXML> <fichierJSON>");
            return;
        }
        
        String nomFichierXML = args[0];
        String nomFichierJSON = args[1];
        
        System.out.println("Debut de la conversion du fichier " + nomFichierXML + " vers le fichier " + nomFichierJSON);

        // Lecture fichier xml avec SAXP et handler custom
         try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XmlHandlerParser handler = new XmlHandlerParser();
            
            saxParser.parse(nomFichierXML, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        System.out.println("Conversion terminee.");
    }

    // private void 
    
}
