package com.ift287lalonde;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Gestionnaire d'une connexion avec une BD NoSQL via MongoDB.
 * 
 * <pre>
 * 
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 2.0 - 6 novembre 2023
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'ouvrir une connexion avec une BD via MongoDB.
 * 
 * Pre-condition
 *   La base de données MongoDB doit être accessible.
 * 
 * Post-condition
 *   La connexion est ouverte.
 * </pre>
 */
public class Connexion
{
    private final MongoClient client;
    private final MongoDatabase database;

    /**
     * Ouverture d'une connexion
     * 
     * @param serveur serveur à utiliser (local ou dinf)
     * @param bd nom de la base de données
     * @param user userid sur le serveur MongoDB pour la BD spécifiée
     * @param pass mot de passe sur le serveur MongoDB pour la BD spécifiée
     */
    public Connexion(String serveur, String bd, String user, String pass) throws IFT287Exception
    {
    	if (serveur.equals("local"))
        {
            client = MongoClients.create();
        }
        else if (serveur.equals("dinf"))
        {
        	client = MongoClients.create("mongodb://"+user+":"+pass+"@bd-info2.dinf.usherbrooke.ca:27017/"+bd+"?ssl=true");
        }
        else
        {
            throw new IFT287Exception("Serveur inconnu");
        }
        
    	database = client.getDatabase(bd);
    	
    	System.out.println("Ouverture de la connexion :");
    	System.out.println("  Connecté sur la BD MongoDB " + bd + " avec l'utilisateur " + user);
    }

    /**
     * Fermeture d'une connexion
     */
    public void fermer()
    {
        client.close();
        System.out.println("Connexion fermée");
    }
    
    
    /**
     * Retourne la Connection MongoDB
     */
    public MongoClient getConnection()
    {
        return client;
    }
    
    /**
     * Retourne la DataBase MongoDB
     */
    public MongoDatabase getDatabase()
    {
        return database;
    }
}
