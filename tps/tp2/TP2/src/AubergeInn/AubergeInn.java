// Travail fait par :
//   SimonLalonde - 22225192

package AubergeInn;

import Tuples.TupleChambre;
import Tuples.TupleClient;
import Tables.TableClient;

import Manager.ManagerClient;

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
    public static void main(String[] args) throws Exception
    {
        if (args.length < 4)
        {
            System.out.println("Usage: java AubergeInn.AubergeInn <serveur> <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        
        cx = null;
        
        try
        {
            
            // Creation de la connection
            cx = new Connexion(args[0], args[1], args[2], args[3]);
            
            // Container pour les tables et les managers
            manager = new MainManager(cx);

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
            if (cx != null)
                cx.fermer();
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
                else if (command.equals("afficherClients")) {
                    manager.getManagerClient().afficherClients();
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
            cx.rollback();
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
