package com.ift287lalonde;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.*;

/**
 * Interface du système de gestion d'une bibliothèque
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'appeler les transactions de base d'une
 * systeme de reservation d'hotel.  Il gere les clients, les reservations
 * les chambres et les commodites. Les données sont conservées dans une 
 * base de données NoSQL accédée avec MongoDB. Pour une liste des
 * transactions traitées, voir la méthode afficherAide().
 *
 * Paramètres
 * 0- serveur
 * 1- nom de la BD
 * 2- user id pour établir une connexion avec le serveur MongoDB
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pré-condition
 *   La base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   Le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */
public class AubergeInn
{
    private final MainManager mainManager;
    private boolean echo;

    /**
     * Ouverture de la BD, traitement des transactions et fermeture de la BD.
     */
    public static void main(String[] argv)
    {
        // validation du nombre de paramètres
        if (argv.length < 4)
        {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password>  [<fichier-transactions>]");
            return;
        }

        AubergeInn mainManager = null;
        
        try
        {
            // ouverture du fichier de transactions
            // s'il est spécifié comme argument
            boolean lectureAuClavier = true;
            InputStream sourceTransaction = System.in;
            if (argv.length > 4)
            {
                sourceTransaction = new FileInputStream(argv[4]);
                lectureAuClavier = false;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction));

            mainManager = new AubergeInn(argv[0], argv[1], argv[2], argv[3]);
            mainManager.setFaireEcho(!lectureAuClavier);
            mainManager.traiterTransactions(reader);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        finally
        {
            if(mainManager != null)
                mainManager.fermer();
        }
    }
    
    public AubergeInn(String serveur, String bd, String user, String pass) throws Exception
    {
        mainManager = new MainManager(serveur, bd, user, pass);
    }
    
    public void setFaireEcho(boolean echo)
    {
        this.echo = echo;
    }
    
    public void fermer()
    {
        mainManager.fermer();
    }

    /**
     * Traitement des transactions de la bibliothèque
     */
    public void traiterTransactions(BufferedReader reader) throws Exception
    {
        afficherAide();
        String transaction = lireTransaction(reader);
        while (!finTransaction(transaction))
        {
            // découpage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens())
                executerTransaction(tokenizer);
            transaction = lireTransaction(reader);
        }
    }

    /**
     * Lecture d'une transaction
     */
    private String lireTransaction(BufferedReader reader) throws IOException
    {
        System.out.print("> ");
        String transaction = reader.readLine();
        // echo si lecture dans un fichier
        if (echo)
            System.out.println(transaction);
        return transaction;
    }

    /**
     * Décodage et traitement d'une transaction
     */
    private void executerTransaction(StringTokenizer tokenizer) throws Exception
    {
        try
        {
            String command = tokenizer.nextToken();

            switch (command) {
                // *******************
                // COMMENTAIRE
                // *******************
                case "//":
                    break;
                // *******************
                // HELP
                // *******************
                case "aide":
                    afficherAide();
                    break;
                // *******************
                // AJOUTER CLIENT
                // *******************
                case "ajouterClient": {
                    // Lecture token client
                    int idClient = readInt(tokenizer);
                    String prenom = readString(tokenizer);
                    String nom = readString(tokenizer);
                    int age = readInt(tokenizer);
                    
                    // Creation client et ajout db
                    TupleClient client = new TupleClient(idClient, prenom, nom, age);
                    mainManager.getManagerClient().ajouterClient(client);
                    System.out.println("\nAjout client idClient=" + client.getId() + " avec succes.");
                    break;
                }
                // *******************
                // SUPPRIMER UN CLIENT
                // *******************
                case "supprimerClient": {
                    // Sauvegarde du idClient
                    int idClient = readInt(tokenizer);

                    // Suppression client
                    mainManager.getManagerClient().supprimerClient(idClient);
                    break;
                }
                case "afficherClient": {
                    // Lecture token idClient
                    int idClient = readInt(tokenizer);
                    
                    // Affichage client
                    mainManager.getManagerClient().afficherClient(idClient);
                    break;
                }
                // *******************
                // AJOUTER UNE CHAMBRE
                // *******************
                case "ajouterChambre": {
                    // Lecture token chambre
                    int idChambre = readInt(tokenizer);
                    String nom = readString(tokenizer);
                    String typeLit = readString(tokenizer);
                    int prixBase = readInt(tokenizer);

                    // Ajout chambre dans db
                    TupleChambre chambre = new TupleChambre(idChambre, nom, typeLit, prixBase);
                    mainManager.getManagerChambre().ajouterChambre(chambre);
                    System.out.println("\nAjout chambre idChambre=" + chambre.getId() + " avec succes.");
                    break;
                }
                // *******************
                // SUPPRIMER UNE CHAMBRE
                // *******************
                case "supprimerChambre": {
                    // Lecute token idChambre
                    int idChambre = readInt(tokenizer);

                    // Suppression chambre dans db
                    mainManager.getManagerChambre().supprimerChambre(idChambre);
                    break;
                }
                // *******************
                // AJOUTER UNE COMMODITE
                // *******************
                case "ajouterCommodite": {
                    // Lecture token commodite
                    int idCommodite = readInt(tokenizer);
                    String description = readString(tokenizer);
                    int surplusPrix = readInt(tokenizer);

                    // Ajout commodite dans db
                    TupleCommodite commodite = new TupleCommodite(idCommodite, description, surplusPrix);
                    mainManager.getManagerCommodite().ajouterCommodite(commodite);
                    System.out.println("\nAjout commodite idCommodite=" + commodite.getId() + " avec succes.");
                    break;
                }
                // *******************
                // SUPPRIMER UNE COMMODITE
                // *******************
                case "supprimerCommodite": {
                    // Lecture token idCommodite
                    int idCommodite = readInt(tokenizer);

                    // Supprimer commodite dans db
                    mainManager.getManagerCommodite().supprimerCommodite(idCommodite);
                    break;
                }
                // *******************
                // INCLURE UNE COMMODITE A UNE CHAMBRE
                // *******************
                case "inclureCommodite": {
                     // Lecture token idChambre et idCommodite
                     int idChambre = readInt(tokenizer);
                     int idCommodite = readInt(tokenizer);
 
                     // Inclure commodite dans chambre
                     mainManager.getManagerCommodite().inclureCommodite(idChambre, idCommodite);
                     System.out.println(
                         "\nInclusion de la commodite " +
                         mainManager.getAccesCommodite().getCommodite(idCommodite).getDescription() +
                         " dans la chambre " +
                         mainManager.getAccesChambre().getChambre(idChambre).getNom() +
                         " avec succes.");
                    break;
                }
                // *******************
                // RETIRER UNE COMMODITE A UNE CHAMBRE
                // *******************
                case "enleverCommodite": {
                    // Lecture token idChambre et idCommodite
                    int idChambre = readInt(tokenizer);
                    int idCommodite = readInt(tokenizer);

                    // Enlever commodite d'une chambre
                    mainManager.getManagerCommodite().enleverCommodite(idChambre, idCommodite);
                    System.out.println(
                        "\nSuppression de la commodite " +
                        mainManager.getAccesCommodite().getCommodite(idCommodite).getDescription() +
                        " dans la chambre " +
                        mainManager.getAccesChambre().getChambre(idChambre).getNom() +
                        " avec succes.");
                    break;
                }
                // *******************
                // RÉSERVER UNE CHAMBRE
                // *******************
                case "reserver": {
                    // Lecture token information reservation
                    int idClient = readInt(tokenizer);
                    int idChambre = readInt(tokenizer);
                    Date dateDebut = readDate(tokenizer);
                    Date dateFin = readDate(tokenizer);

                    // Reservation d'une chambre
                    mainManager.getManagerReservation().reserver(idClient, idChambre, dateDebut, dateFin);
                    
                    // Message de confirmation
                    String nomClient = mainManager.getAccesClient().getClient(idClient).getNom();
                    String nomChambre = mainManager.getAccesChambre().getChambre(idChambre).getNom();
                    System.out.println(
                        "\nReservation de la chambre " + nomChambre +
                        " pour le client " + nomClient +
                        " du " + dateDebut.toString() +
                        " au " + dateFin.toString() +
                        " avec succes."
                    );
                    break;
                }
                // *******************
                // CHAMBRES LIBRES
                // *******************
                case "afficherChambresLibres": {
                    // Lecture token dates
                    Date dateDebut = readDate(tokenizer);
                    Date dateFin = readDate(tokenizer);

                    // Affichage des chambres libres
                    mainManager.getManagerChambre().afficherChambresLibres(dateDebut, dateFin);
                    break;
                }
                // *******************
                // ANNULER RÉSERVATION
                // *******************
                case "afficherChambre": {
                    // Lecture token idChambre
                    int idChambre = readInt(tokenizer);

                    // affichage de la chambre si existe
                    mainManager.getManagerChambre().afficherChambre(idChambre);
                    break;
                }
                // *********************
                // commentaire : ligne débutant par --
                // *********************
                case "--":
                    // ne rien faire, c'est un commentaire
                    break;
                // ***********************
                // EXIT
                // ***********************
                case "quitter": {
                    System.out.println("Fin du programme");
                    mainManager.fermer();
                    System.exit(0);
                    break;
                }

                // ***********************
                // TRANSACTION INCONNUE
                // ***********************
                default:
                    System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
                    break;
            }
        }
        catch (IFT287Exception e)
        {
            System.out.println("** " + e);
        }
        catch (Exception e)
        {
            System.out.println("** Erreur non gérée : " + e);
            throw e;
        }
    }

    /** Affiche le menu des transactions acceptées par le système */
    private static void afficherAide()
    {
        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("séparés par des espaces. La liste peut être vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.");
        System.out.println();
        System.out.println("Les transactions sont:");
        System.out.println("  aide");
        System.out.println("  ajouterClient <idClient> <prenom> <nom> <age>");
        System.out.println("  supprimerClient <idClient>");
        System.out.println("  afficherClient <idClient>");
        System.out.println("  ajouterChambre <idChambre> <nom> <typeLit> <prixBase>");
        System.out.println("  supprimerChambre <idChambre>");
        System.out.println("  ajouterCommodite <idCommodite> <description> <surplusPrix>");
        System.out.println("  supprimerCommodite <idCommodite>");
        System.out.println("  inclureCommodite <idChambre> <idCommodite>");
        System.out.println("  enleverCommodite <idChambre> <idCommodite>");
        System.out.println("  reserver <idClient> <idChambre> <dateDebut> <dateFin>");
        System.out.println("  afficherChambresLibres <dateDebut> <dateFin>");
        System.out.println("  afficherChambre <idChambre>");
        System.out.println("  // ou -- pour un commentaire");
        System.out.println("  quitter");
        
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     */
    private boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        if (transaction == null)
            return true;

        StringTokenizer tokenizer = new StringTokenizer(transaction, " ");

        // ligne ne contenant que des espaces
        if (!tokenizer.hasMoreTokens())
            return false;

        // commande "exit"
        String commande = tokenizer.nextToken();
        return commande.equals("exit");

    }

    /** Lecture d'une chaîne de caractères de la transaction entrée à l'écran */
    private String readString(StringTokenizer tokenizer) throws IFT287Exception
    {
        if (tokenizer.hasMoreElements())
        {
            return tokenizer.nextToken();
        }
        else
        {
            throw new IFT287Exception("autre paramètre attendu");
        }
    }

    /**
     * Lecture d'un int Java de la transaction entrée à l'écran
     */
    private int readInt(StringTokenizer tokenizer) throws IFT287Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Integer.parseInt(token);
            }
            catch (NumberFormatException e)
            {
                throw new IFT287Exception("Nombre attendu à la place de \"" + token + "\"");
            }
        }
        else
        {
            throw new IFT287Exception("autre paramètre attendu");
        }
    }

    /**
     * Lecture d'un long Java de la transaction entrée à l'écran
     */
    private long readLong(StringTokenizer tokenizer) throws IFT287Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Long.parseLong(token);
            }
            catch (NumberFormatException e)
            {
                throw new IFT287Exception("Nombre attendu à la place de \"" + token + "\"");
            }
        }
        else
        {
            throw new IFT287Exception("autre paramètre attendu");
        }
    }

    /**
     * Lecture d'une date en format YYYY-MM-DD
     */
    private Date readDate(StringTokenizer tokenizer) throws IFT287Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return new SimpleDateFormat("yyyy-MM-dd").parse(token);
            }
            catch (ParseException e)
            {
                throw new IFT287Exception("Date en format YYYY-MM-DD attendue à la place  de \"" + token + "\"");
            }
        }
        else
        {
            throw new IFT287Exception("autre paramètre attendu");
        }
    }
}
