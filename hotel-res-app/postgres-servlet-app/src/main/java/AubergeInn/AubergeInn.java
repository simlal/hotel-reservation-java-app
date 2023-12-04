// Travail fait par :
//   SimonLalonde - 22225192

package AubergeInn;

import java.io.*;
import java.util.StringTokenizer;
import java.sql.*;

/**
 * Fichier de base pour le TP2 du cours IFT287
 *
 * <pre>
 * 
 * Kerson Boisrond
 * Universite de Sherbrooke
 * Version 2.0 - 18 Septembre 2023
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'appeler des transactions d'un systeme
 * de gestion utilisant une base de donnees.
 *
 * Paramètres du programme
 * 0- site du serveur SQL ("local" ou "dinf")
 * 1- nom de la BD
 * 2- user id pour etablir une connexion avec le serveur SQL
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pré-condition
 *   - La base de donnees doit exister
 *
 * Post-condition
 *   - Le programme effectue les mises à jour associees à chaque
 *     transaction
 * </pre>
 */
public class AubergeInn
{
    private static Connexion cx;
    private static MainManager manager;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception, IFT287Exception
    {
        if (args.length < 4)
        {
            System.out.println("Usage: java AubergeInn.AubergeInn <serveur> <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        
        try
        {
            

            // Creation connexion et gestionnaire principal
            manager = new MainManager(args[0], args[1], args[2], args[3]);

            BufferedReader reader = ouvrirFichier(args);
            String transaction = lireTransaction(reader);
            while (!finTransaction(transaction))
            {
                executerTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        }
        finally
        {
            if (manager.getConnexion() != null)
                manager.fermer();
        }
    }

    /**
     * Decodage et traitement d'une transaction
     */
    static void executerTransaction(String transaction) throws Exception, IFT287Exception
    {
        try
        {
            System.out.print(transaction);
            // Decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens())
            {
                String command = tokenizer.nextToken();
                if (command.equals("//")) {
                    // commentaire, on ne fait rien
                }
                
                else if (command.equals("ajouterClient")) {
                    // Lecture token client
                    int idClient = readInt(tokenizer);
                    String prenom = readString(tokenizer);
                    String nom = readString(tokenizer);
                    int age = readInt(tokenizer);
                    
                    // Creation client et ajout db
                    TupleClient nouveauClient = new TupleClient(idClient, prenom, nom, age);
                    manager.getManagerClient().ajouterClient(nouveauClient);
                    
                }
                
                else if (command.equals("supprimerClient")) {
                    // Sauvegarde du idClient
                    int idClient = readInt(tokenizer);

                    // Suppression client
                    manager.getManagerClient().supprimerClient(idClient);
                }
                else if (command.equals("afficherClient")) {
                    // Lecture token idClient
                    int idClient = readInt(tokenizer);
                    
                    // Affichage client
                    manager.getManagerClient().afficherClient(idClient);
                }
                else if (command.equals("ajouterChambre")) {
                    // Lecture token chambre
                    int idChambre = readInt(tokenizer);
                    String nom = readString(tokenizer);
                    String typeLit = readString(tokenizer);
                    int prixBase = readInt(tokenizer);

                    // Ajout chambre dans db
                    TupleChambre chambre = new TupleChambre(idChambre, nom, typeLit, prixBase);
                    manager.getManagerChambre().ajouterChambre(chambre);
                }
                else if (command.equals("supprimerChambre")) {
                    // Lecute token idChambre
                    int idChambre = readInt(tokenizer);

                    // Suppression chambre dans db
                    manager.getManagerChambre().supprimerChambre(idChambre);
                }
                else if (command.equals("ajouterCommodite")) {
                    // Lecture token commodite
                    int idCommodite = readInt(tokenizer);
                    String description = readString(tokenizer);
                    int surplusPrix = readInt(tokenizer);

                    // Ajout commodite dans db
                    TupleCommodite commodite = new TupleCommodite(idCommodite, description, surplusPrix);
                    manager.getTableCommodite().ajouterCommodite(commodite);
                }

                else if (command.equals("supprimerCommodite")) {
                    // Lecture token idCommodite
                    int idCommodite = readInt(tokenizer);

                    // Supprimer commodite dans db
                    manager.getManagerCommodite().supprimerCommodite(idCommodite);
                }

                else if (command.equals("inclureCommodite")) {
                    // Lecture token idChambre et idCommodite
                    int idChambre = readInt(tokenizer);
                    int idCommodite = readInt(tokenizer);

                    // Inclure commodite dans chambre
                    TupleChambreCommodite chambreCommodite = new TupleChambreCommodite(idChambre, idCommodite);
                    manager.getManagerChambreCommodite().inclureChambreCommodite(chambreCommodite);
                }

                else if (command.equals("enleverCommodite")) {
                    // Lecture token idChambre et idCommodite
                    int idChambre = readInt(tokenizer);
                    int idCommodite = readInt(tokenizer);

                    // Enlever commodite d'une chambre
                    TupleChambreCommodite chambreCommodite = new TupleChambreCommodite(idChambre, idCommodite);
                    manager.getManagerChambreCommodite().enleverChambreCommodite(chambreCommodite);
                }

                else if (command.equals("reserver")) {
                    // Lecture token information reservation
                    int idClient = readInt(tokenizer);
                    int idChambre = readInt(tokenizer);
                    Date dateDebut = readDate(tokenizer);
                    Date dateFin = readDate(tokenizer);

                    // Reservation d'une chambre
                    TupleReservation reservation = new TupleReservation(dateDebut, dateFin, idClient, idChambre);
                    manager.getManagerReservation().faireReservation(reservation);
                }

                else if (command.equals("afficherChambresLibres")) {
                    // Lecture token dates
                    Date dateDebut = readDate(tokenizer);
                    Date dateFin = readDate(tokenizer);

                    // Affichage des chambres libres
                    manager.getManagerChambre().afficherChambresLibres(dateDebut, dateFin);
                }

                else if (command.equals("afficherChambre")) {
                    // Lecture token idChambre
                    int idChambre = readInt(tokenizer);

                    // affichage de la chambre si existe
                    manager.getManagerChambre().afficherChambre(idChambre);
                }
                // Pas besoin de traiter quitter
                else
                {
                    System.out.println(" : Transaction non reconnue");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(" " + e.toString());
            // Ce rollback est ici seulement pour vous aider et éviter des problèmes lors de la correction
            // automatique. En théorie, il ne sert à rien et ne devrait pas apparaître ici dans un programme
            // fini et fonctionnel sans bogues.
            manager.getConnexion().rollback();
        }
    }

    
    // ****************************************************************
    // *   Les methodes suivantes n'ont pas besoin d'etre modifiees   *
    // ****************************************************************

    /**
     * Ouvre le fichier de transaction, ou lit à partir de System.in
     */
    public static BufferedReader ouvrirFichier(String[] args) throws FileNotFoundException
    {
        if (args.length < 5)
            // Lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // Lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(new FileInputStream(args[4])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException
    {
        System.out.println("");
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        return (transaction == null || transaction.equals("quitter"));
    }

    /** Lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("Autre parametre attendu");
    }

    /**
     * Lecture d'un int java de la transaction entree a l'ecran
     */
    static int readInt(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Integer.valueOf(token).intValue();
            }
            catch (NumberFormatException e)
            {
                throw new Exception("Nombre attendu a la place de \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

    static Date readDate(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Date.valueOf(token);
            }
            catch (IllegalArgumentException e)
            {
                throw new Exception("Date dans un format invalide - \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

}
