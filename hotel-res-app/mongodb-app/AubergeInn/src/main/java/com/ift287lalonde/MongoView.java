package com.ift287lalonde;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Affichage des données d'une BD NoSQL MongoDB.
 * 
 * <pre>
 * 
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 2.0 - 14 novembre 2023
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'afficher toutes les données d'une base de données NoSQL MongoDB.
 * 
 * Pre-condition
 *   La base de données MongoDB doit être accessible.
 * </pre>
 */
public class MongoView
{
    public static void main(String[] args)
    {
        if (args.length < 4)
        {
            System.out.println("Usage: java MongoView <serveur> <bd> <user> <password>");
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

                System.out.println("Documents de la collection : " + collName);

                try (MongoCursor<Document> cursor = coll.find().iterator()) {
                    while (cursor.hasNext()) {
                        System.out.println(cursor.next());
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                System.out.println();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
