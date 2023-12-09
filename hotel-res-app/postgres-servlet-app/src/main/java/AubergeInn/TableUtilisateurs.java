package AubergeInn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet d'effectuer les accès à la table utilisateur.
 *
 * <pre>
 *
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
 * Cette classe gère tous les accès à la table d'utilisateurs.
 *
 * </pre>
 */

public class TableUtilisateurs
{
    private PreparedStatement stmtExisteUtilisateur;
    private PreparedStatement stmtListeUtilisateur;
    private PreparedStatement stmtInsert;
    private PreparedStatement stmtDelete;
    private Connexion cx;

    public TableUtilisateurs(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtListeUtilisateur = cx.getConnection().prepareStatement(
                "SELECT utilisateurName, motDePasse, acces, nom, telephone FROM Utilisateurs WHERE acces = ?");
        stmtExisteUtilisateur = cx.getConnection().prepareStatement(
                "SELECT utilisateurName, motDePasse, acces, nom, telephone FROM Utilisateurs WHERE utilisateurName = ?");
        stmtInsert = cx.getConnection().prepareStatement(
                "INSERT INTO Utilisateurs (utilisateurName, motDePasse, acces, nom, telephone) "
                        + "VALUES (?,?,?,?,?)");
        stmtDelete = cx.getConnection().prepareStatement("DELETE FROM Utilisateurs WHERE utilisateurName = ?");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Verifie si un utilisateur existe.
     */
    public boolean existe(String utilisateurName) throws SQLException
    {
        stmtExisteUtilisateur.setString(1, utilisateurName);
        ResultSet rset = stmtExisteUtilisateur.executeQuery();
        boolean utilisateurExiste = rset.next();
        rset.close();
        return utilisateurExiste;
    }

    /**
     * Lecture d'un utilisateur a partir de son nom
     */
    public TupleUtilisateur getUtilisateur(String utilisateurName) throws SQLException
    {
        stmtExisteUtilisateur.setString(1, utilisateurName);
        ResultSet rset = stmtExisteUtilisateur.executeQuery();
        TupleUtilisateur tupleUtilisateur = null;
        if (rset.next())
        {
            String motDePasse = rset.getString(2);
            int acces = rset.getInt(3);
            String nom = rset.getString(4);
            long telephone = rset.getLong(5);

            tupleUtilisateur = new TupleUtilisateur(utilisateurName, motDePasse, acces, nom, telephone);
            rset.close();
        }
        return tupleUtilisateur;
    }

    /**
     * Ajout d'un nouveau utilisateur.
     */
    public void inscrire(String utilisateurName, String motDePasse, int acces, String nom, long telephone)
            throws SQLException
    {
        stmtInsert.setString(1, utilisateurName);
        stmtInsert.setString(2, motDePasse);
        stmtInsert.setInt(3, acces);
        stmtInsert.setString(4, nom);
        stmtInsert.setLong(5, telephone);
        stmtInsert.executeUpdate();
    }


    /**
     * Suppression d'un utilisateur.
     */
    public int desinscrire(String utilisateurName) throws SQLException
    {
        stmtDelete.setString(1, utilisateurName);
        return stmtDelete.executeUpdate();
    }

    public List<TupleUtilisateur> getListUtilisateurs(boolean avecAdmin) throws SQLException
    {
        try {
            List<TupleUtilisateur> utilisateurs = new ArrayList<TupleUtilisateur>();
            stmtListeUtilisateur.setInt(1, 1);
            ResultSet rset = stmtListeUtilisateur.executeQuery();
            while (rset.next()) {
                String utilisateurName = rset.getString(1);
                String motDePasse = rset.getString(2);
                int acces = rset.getInt(3);
                String nom = rset.getString(4);
                long telephone = rset.getLong(5);


                TupleUtilisateur tupleUtilisateur = new TupleUtilisateur(utilisateurName, motDePasse, acces, nom, telephone);
                utilisateurs.add(tupleUtilisateur);
            }
            if (avecAdmin) {
                stmtListeUtilisateur.setInt(1, 0);
                rset = stmtListeUtilisateur.executeQuery();
                while (rset.next()) {
                    String utilisateurName = rset.getString(1);
                    String motDePasse = rset.getString(2);
                    int acces = rset.getInt(3);
                    String nom = rset.getString(4);
                    long telephone = rset.getLong(5);

                    TupleUtilisateur tupleUtilisateur = new TupleUtilisateur(utilisateurName, motDePasse, acces, nom, telephone);
                    utilisateurs.add(tupleUtilisateur);
                }
            }
            rset.close();
            return utilisateurs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur getListUtilisateurs dans TableUtilisateur");
        }
    }
}