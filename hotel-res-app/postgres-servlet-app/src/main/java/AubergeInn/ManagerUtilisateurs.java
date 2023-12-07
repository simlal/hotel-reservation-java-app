package AubergeInn;

import java.sql.*;
import java.util.List;

/**
 * Gestion des transactions de reliées à la gestion des utilisateurs de l'application
 * AubergeInn.
 *
 * <pre>
 * Marc Frappier
 * Université de Sherbrooke
 * Version 2.0 - 13 novembre 2004
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 3.0 - 11 novembre 2018
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Pré-condition
 *   la base de données de AubergeInn doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */

public class ManagerUtilisateurs
{
    private Connexion cx;
    private TableUtilisateurs utilisateurs;


    /**
     * Creation d'une instance
     */
    public ManagerUtilisateurs(TableUtilisateurs utilisateurs) throws IFT287Exception
    {
        this.cx = utilisateurs.getConnexion();
        this.utilisateurs = utilisateurs;
    }

    public boolean existe(String utilisateur)
            throws SQLException
    {
        return this.utilisateurs.existe(utilisateur);
    }

    public boolean informationsConnexionValide(String utilisateur, String motDePasse)
            throws SQLException, IFT287Exception
    {
        try
        {
            // Vérifie si le membre existe déja
            if (!utilisateurs.existe(utilisateur))
                throw new IFT287Exception("Aucun utilisateur n'existe avec ce nom.");


            TupleUtilisateur user = utilisateurs.getUtilisateur(utilisateur);
            if(!user.getMotDePasse().equals(motDePasse))
                throw new IFT287Exception("Mauvais mot de passe.");

            // Commit
            cx.commit();

            return true;
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    public boolean utilisateurEstAdministrateur(String utilisateur)
            throws SQLException, IFT287Exception
    {
        try
        {
            TupleUtilisateur u = utilisateurs.getUtilisateur(utilisateur);
            if(u == null)
                throw new IFT287Exception("L'utilisateur n'existe pas");

            cx.commit();
            return u.getNiveauAcces() == 0;
        }
        catch(Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Ajout d'un nouveau membre dans la base de donnees. S'il existe deja, une
     * exception est levee.
     */
    public void inscrire(String utilisateur, String motDePasse, int acces, String nom, long telephone)
            throws SQLException, IFT287Exception, Exception
    {
        try
        {
            // Vérifie si le membre existe déja
            if (utilisateurs.existe(utilisateur))
                throw new IFT287Exception("L'utilisateur " + utilisateur + " existe déjà.");

            if(acces != 0 && acces != 1)
                throw new IFT287Exception("Le niveau d'accès doit être 0 (administrateur) ou 1 (membre).");

            // Ajout du membre.
            utilisateurs.inscrire(utilisateur, motDePasse, acces, nom, telephone);

            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    /**
     * Suppression d'un membre de la base de donnees.
     */
    public void desinscrire(String utilisateur) throws SQLException, IFT287Exception, Exception
    {
        try
        {
            // Vérifie si le membre existe et son nombre de pret en cours
            TupleUtilisateur tupleUtilisateur = utilisateurs.getUtilisateur(utilisateur);
            if (tupleUtilisateur == null)
                throw new IFT287Exception("Membre inexistant: " + utilisateur);

            // Suppression du membre
            int nb = utilisateurs.desinscrire(utilisateur);
            if (nb == 0)
                throw new IFT287Exception("Membre " + utilisateur + " inexistant");

            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }

    public List<TupleUtilisateur> getListeUtilisateurs(boolean avecAdmin) throws SQLException, IFT287Exception
    {
        try
        {
            List<TupleUtilisateur> listUtilisateurs = utilisateurs.getListUtilisateurs(avecAdmin);
            cx.commit();
            return listUtilisateurs;
        }
        catch(Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}// class