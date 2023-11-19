package com.ift287lalonde;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Suppression des données d'une BD NoSQL MongoDB.
 *
 * <pre>
 *
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 2.0 - 14 novembre 2023
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Ce programme permet de supprimer toutes les données d'une base de données NoSQL MongoDB.
 *
 * Pre-condition
 *   La base de données MongoDB doit être accessible.
 * </pre>
 */
public class MongoCleanUp
{
    public static void main(String[] args)
    {
        if (args.length < 4)
        {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password>");
            return;
        }

        try {
            Connexion cx = new Connexion(args[0], args[1], args[2], args[3]);
            MongoDatabase db = cx.getDatabase();

            MongoIterable<String> collNames = db.listCollectionNames();
            for (String collName : collNames)
            {
                if (collName.equals("system.version") || collName.equals("system.users"))
                    continue;

                MongoCollection<Document> coll = db.getCollection(collName);
                coll.drop();
                System.out.println("La collection " + collName + " a été supprimée.");
            }
            System.out.println("La base de données MongoDB " + args[1] + " a été vidée.");
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

}
