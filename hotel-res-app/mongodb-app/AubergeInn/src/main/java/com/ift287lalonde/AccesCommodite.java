package com.ift287lalonde;

import org.bson.Document;
import static com.mongodb.client.model.Updates.push;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;


public class AccesCommodite {
    
    private Connexion cx;
    private MongoCollection<Document> commoditesCollection;

    public AccesCommodite(Connexion cx) {
        this.cx = cx;
        this.commoditesCollection = cx.getDatabase().getCollection("commodites");
    }

    /**
     * Retourne la connexion
     * 
     * @return Connexion
     */
    public Connexion getConnexion() {
        return cx;
    }

    /**
     * Retourne la collection
     * 
     * @return commoditeCollection
     */
    public MongoCollection<Document> getCommoditesCollection() {
        return commoditesCollection;
    }

    /**
     * Verifie si commodite existe
     * 
     * @param idCommodite
     * @return true si la commodite existe, false sinon
     */
    public boolean commoditeExiste(int idCommodite) {
        boolean commoditeExiste = false;
        if (commoditesCollection.find(eq("idCommodite", idCommodite)).first() != null) {
            commoditeExiste = true;
        }
        return commoditeExiste;
    }

    /**
     * Cherche la commodite avec l'id fourni
     * 
     * @param idCommodite
     * @return TupleCommodite
     */
    public TupleCommodite getCommodite(int idCommodite) {
        TupleCommodite commodite = null;
        if (commoditeExiste(idCommodite)) {
            Document commoditeDoc = commoditesCollection.find(eq("idCommodite", idCommodite)).first();
            commodite = new TupleCommodite(commoditeDoc);
        }
        return commodite;
    }

    /**
     * Ajoute une commodite dans la db
     * 
     * @param commodite
     */
    public void ajouterCommodite(TupleCommodite commodite) {
        commoditesCollection.insertOne(commodite.toDocument());
    }

    /**
     * Supprime une commodite de la db
     *
     * @param idCommodite 
     */
    public boolean supprimerCommodite(int idCommodite) {
        boolean commoditeSupprime = commoditesCollection
            .deleteOne(eq("idCommodite", idCommodite))
            .getDeletedCount() > 0;
        return commoditeSupprime;
    }

    /**
     * Faire le lien entre la chambre et la commodite
     * 
     * @param chambre
     * @param commodite
     */
    public boolean inclureCommodite(TupleChambre chambre, TupleCommodite commodite) {
        UpdateResult result = commoditesCollection
            .updateOne(
                eq("idCommodite", commodite.getId()),
                push("chambresId", chambre.getId())
            );
        boolean commoditeIncluse = result.getModifiedCount() > 0;
        return commoditeIncluse;
    }

    /**
     * Retirer le lien entre chambre et commodite
     * 
     * @param chambre
     * @param commodite
     */
    public boolean enleverCommodite(TupleChambre chambre, TupleCommodite commodite) {
        UpdateResult result = commoditesCollection
            .updateOne(
                eq("idCommodite", commodite.getId()),
                pull("chambresId", chambre.getId())
            );
        boolean commoditeRetiree = result.getModifiedCount() > 0;
        return commoditeRetiree;
    }

    /**
     * Lister commodites d'une chambre
     * 
     * @param idChambre
     * @return commodites
     */
    public List<TupleCommodite> getCommoditesChambre(int idChambre) {
        // Trouver commodites associes a une chambre
        MongoCursor<Document> commoditesCursor = commoditesCollection
            .find(in("chambresId", idChambre))
            .iterator();
        List<TupleCommodite> commodites = new ArrayList<>();

        try {
            while (commoditesCursor.hasNext()) {
                Document commoditeDoc = commoditesCursor.next();
                TupleCommodite commodite = new TupleCommodite(commoditeDoc);
                commodites.add(commodite);
            }
        } finally {
            commoditesCursor.close();
        }
        return commodites;
    }
}
